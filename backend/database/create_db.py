from flask import Flask
from db import db
import database
import os, urllib2

app = Flask(__name__)
config_file = os.path.dirname(os.path.realpath(__file__)) + '/../config.py'
app.config.from_pyfile(config_file)
db.init_app(app)

from models import User, Project, Skill, Weighted_Skill, Swipe, Login, Match
from devs_to_projects import devs_to_projects
from project_skills import project_skills
from user_skills import user_skills
from faker import Faker
import random

fake = Faker()
app.app_context().push()

# If this file is run as a script
if __name__ == '__main__':
    db.drop_all()
    db.create_all()

    # If the flag is set in config.py
    if app.config['INSERT_TEST_DATA'] == True:
        skill_arr = [
            Skill(skill_name="Python"),
            Skill(skill_name="Django"),
            Skill(skill_name="MySQL"),
            Skill(skill_name="SQLAlchemy"),
            Skill(skill_name="C"),
            Skill(skill_name="C++"),
            Skill(skill_name="PHP"),
            Skill(skill_name="Scala"),
            Skill(skill_name="Microsoft"),
        ]

        for s in skill_arr:
            database.insert_obj(s)
            #user_arr[1].skill_sets.append(s)
            #user_arr[2].skill_sets.append(s)
            db.session.commit()

        """
        user 1 is desperate and has swiped on all projects
        user 2 is pro has swiped on all projects and all projects have swiped on him (matched yes on all projects)
        user 3 is a great developer and has a super high desired salary ($200/hr)
        """
        user_arr = [
            User(first_name="John", last_name="Dough", email="xyz", username="jd", bio="m4st3r h4x0r"),
            User(first_name="Mike", last_name="Li", email="mail", username="mic", bio="admin"),
            User(first_name="Rich", last_name="Sun", email="rich", username="rich", bio="waffle the bunny"),
            User(first_name="Vince", last_name="Jin", email="jinir", username="vince", bio="flask dev")
        ]
        #set high salary for user 3
        user_arr[2].desired_salary = 200

        for i in range(25):
            fname = fake.first_name()
            lname = fake.last_name()
            email = fake.company_email()
            uname = fake.user_name()
            bio = fake.sentence()
            new_user = User(first_name=fname, last_name=lname, email=email, username=uname, bio=bio)
            new_user.desired_salary = random.randint(0, 50)
            user_arr.append(new_user)
        for u in user_arr:
            #generate few unique skills for the user
            #last_skill = -1
            skill_set = set()
            for j in range(5):
                skill = random.randint(0, len(skill_arr) - 1)
                #while skill == last_skill:
                while skill in skill_set:
                    skill = random.randint(0, len(skill_arr) - 1)
                skill_set.add(skill)
                u.skill_sets.append(skill_arr[skill])
                #last_skill = skill
            
            database.insert_obj(u)
            login = Login(username=u.username, password="hunter2")
            database.insert_obj(login)


        project_arr = [
            Project(title="H4cks", description="M4st3r h4cks 4 dayz", pm_id=user_arr[0].id),
            Project(title="H4cks2", description="M4st3r h4cks again", pm_id=user_arr[3].id),
            Project(title="H4cks3", description="M4st3r h4cks in Minecraft", pm_id=user_arr[3].id),
            Project(title="H4cks4", description="M4st3r h4cks IRL", pm_id=user_arr[2].id)
        ]
        for i in range(25):
            title = ' '.join(fake.words())
            desc = fake.paragraph()
            pm = user_arr[random.randint(0, len(user_arr) - 1)].id
            project_arr.append(Project(title=title, description=desc, pm_id=pm))
        for p in project_arr:
            p.pay_range = random.randint(50, 225)
            database.insert_obj(p)
            user_arr[random.randint(0, len(user_arr) - 1)].projects_developing.append(p)
            # generate a few unique skills for each project
            skill_set = set()
            #last_skill = -1
            for j in range(5):
                skill = random.randint(0, len(skill_arr) - 1)
                #while skill == last_skill:
                while skill in skill_set:
                    skill = random.randint(0, len(skill_arr) - 1)
                skill_set.add(skill)
                p.skills_needed.append(skill_arr[skill])
                p.skill_weights.append(Weighted_Skill(p.id, skill_arr[skill].id, 5.0))
                #last_skill = skill
            db.session.commit()

        swipe_arr = [
            #Swipe(user_id=user_arr[1].id, project_id=project_arr[0].id, result=Swipe.RESULT_YES, who_swiped=Swipe.SWIPER_DEV),
            #Swipe(user_id=user_arr[1].id, project_id=project_arr[0].id, result=Swipe.RESULT_YES, who_swiped=Swipe.SWIPER_PM),
            #Swipe(user_id=user_arr[3].id, project_id=project_arr[1].id, result=Swipe.RESULT_YES, who_swiped=Swipe.SWIPER_DEV),
            #Swipe(user_id=user_arr[0].id, project_id=project_arr[2].id, result=Swipe.RESULT_NO, who_swiped=Swipe.SWIPER_DEV),
            #Swipe(user_id=user_arr[3].id, project_id=project_arr[3].id, result=Swipe.RESULT_NO, who_swiped=Swipe.SWIPER_DEV),
            #Swipe(user_id=user_arr[3].id, project_id=project_arr[3].id, result=Swipe.RESULT_NO, who_swiped=Swipe.SWIPER_PM)
        ]
        #make user 1 and 2 swipe on all projects and all projects swipe on user 2
        for proj in project_arr:
            more_swipes = [
                Swipe(user_id=user_arr[0].id, project_id=proj.id, result=Swipe.RESULT_YES, who_swiped=Swipe.SWIPER_DEV),
                Swipe(user_id=user_arr[1].id, project_id=proj.id, result=Swipe.RESULT_YES, who_swiped=Swipe.SWIPER_DEV),
                Swipe(user_id=user_arr[1].id, project_id=proj.id, result=Swipe.RESULT_YES, who_swiped=Swipe.SWIPER_PM),
            ]
            #add swipes for users that aren't special for testing
            rand_user_ids = set()
            for i in range(10):
                rand_user = random.randint(4, len(user_arr) - 1)
                while rand_user in rand_user_ids:
                    rand_user = random.randint(4, len(user_arr) - 1)
                rand_user_ids.add(rand_user)
                more_swipes.append(Swipe(user_id=user_arr[rand_user].id,
                    project_id=proj.id, result=Swipe.RESULT_YES,
                    who_swiped=Swipe.SWIPER_DEV))
            rand_user_ids = list(rand_user_ids)
            for i in range(len(rand_user_ids) / 2):
                idx = rand_user_ids[i]
                more_swipes.append(Swipe(user_id=user_arr[idx].id,
                    project_id=proj.id, result=Swipe.RESULT_YES,
                    who_swiped=Swipe.SWIPER_PM))

            swipe_arr += more_swipes
        #database.add_swipe should auto generate the proper Match objects
        """
        match_arr = [
            #good match for user 1, project 0
            Match(user_id=user_arr[1].id, project_id=project_arr[0].id)
        ]
        """
        for sw in swipe_arr:
            database.add_swipe(user_id=sw.user_id, project_id=sw.project_id,
                result=sw.result, who_swiped=sw.who_swiped)
        """
        for m in match_arr:
            database.insert_obj(m)
        """

        # test images
        img_dir = os.path.dirname(os.path.realpath(__file__)) + '/../' + app.config['IMG_PATH']
        user_imgs = [
            "http://i.imgur.com/0qZGhaD.jpg",
            "http://i.imgur.com/2QklmTNr.jpg",
            "http://i.imgur.com/Umv5YFt.png"
        ]
        for i, url in enumerate(user_imgs):
            path = img_dir + '/user/' + str(i+1)
            if os.path.isfile(path):
                continue
            resp = urllib2.urlopen(url)
            with open(path, 'w') as f:
                f.write(resp.read())

        proj_imgs = [
            "http://i.imgur.com/o7E8bHx.jpg",
            "http://i.imgur.com/65RykWH.jpg",
            "http://i.imgur.com/sOkujAg.jpg"
        ]
        for i, url in enumerate(proj_imgs):
            path = img_dir + '/project/' + str(i+1)
            if os.path.isfile(path):
                continue
            resp = urllib2.urlopen(url)
            with open(path, 'w') as f:
                f.write(resp.read())
