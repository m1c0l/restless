from db import db
from models import User, Project, Skill, Swipe, Login, Match
from devs_to_projects import devs_to_projects
from project_skills import project_skills
from user_skills import user_skills

def insert_obj(obj):
    """
    Inserts an object into the database

    @param obj: The object to be inserted
    @type obj: flask_sqlalchemy.Model
    """
    db.session.add(obj)
    db.session.commit()

def update(obj, **kwargs):
    """
    Updates an object in the database. For example, to change a user's first
    name::

        update(user, first_name="new first name")

    @param obj: An existing object in the database
    @type obj: flask_sqlalchemy.Model
    @param kwargs: The attributes to update on I{obj}
    @raise AttributeError: If C{obj} does not have a given attribute
    """
    for key, value in kwargs.items():
        if hasattr(obj, key):
            setattr(obj, key, value)
        else:
            raise AttributeError("%s has no attribute '%s'" % (obj.__class__, key))
    db.session.commit()

def delete(obj):
    """
    Deletes an object from the database

    @param obj: The existing object in the database to be deleted
    @type obj: flask_sqlalchemy.Model
    """
    db.session.delete(obj)
    db.session.commit()

def get_user_by_username(username):
    """
    Get a User by username

    @param username: The user's username
    @type username: C{string}
    @return: The C{User} with the username C{username}, or C{None}
    @rtype: L{User}
    """
    return User.query.filter_by(username=username).first()

def get_user_by_id(id):
    """
    Get a User by id

    @param id: The user's id
    @type id: C{int}
    @return: The C{User} with the id C{id}, or C{None}
    @rtype: L{User}
    """
    return User.query.get(id)

def get_project_by_id(id):
    """
    Get a Project by id

    @param id: The project's id
    @type id: C{int}
    @return: The C{Project} with the id C{id}, or C{None}
    @rtype: L{Project}
    """
    return Project.query.get(id)

def get_project_by_title(title):
    """
    Get a Project by title

    @param title: The project's title
    @type title: C{string}
    @return: The C{Project} with the title C{title}, or C{None}
    @rtype: list of L{Project}
    """
    return Project.query.filter_by(title=title).first()

def get_projects_with_any_skills(skill_list):
    """
    Get Projects that have at least one of the skills listed as required

    @param skill_list: List of skills to search for
    @type skill_list: L{Skill}
    @return: C{Project}s that each have at least one of the skills
    @rtype: L{Project}
    """
    #I know, the below code is really silly but I can't seem to query relationships
    #properly with an in_ function...
    #I would've tried querying the user_skills table directly but our code isn't set up to do that
    skill_ids = []
    project_list = []
    for skill in skill_list:
        #Get all projects with a specific skill, append to list of projects
        proj = Project.query.filter(Project.skills_needed.any(id=skill.id)).all()
        #merge lists to remove duplicates
        project_list = list(set(project_list + proj))
    return project_list
    #flask sqlalchemy relationships are silly and don't support the in_ function
    #which would've made life so much easier...
    #return Project.query.filter(Project.skills_needed.in_(skill_list)).all()

def get_skill_by_id(id):
    """
    Get a Skill by id

    @param id: The skill's id
    @type id: C{int}
    @return: The C{Skill} with the id C{id}, or C{None}
    @rtype: L{Skill}
    """
    return Skill.query.get(id)

def get_skill_by_name(skill_name):
    """
    Get a Skill by username

    @param skill_name: The user's username
    @type skill_name: C{string}
    @return: The C{Skill} with the skill_name C{skill_name}, or C{None}
    @rtype: L{Skill}
    """
    return Skill.query.filter_by(skill_name=skill_name).first()

def get_swipe_by_id(id):
    """
    Get a Swipe by id

    @param id: The swipe's id
    @type id: C{int}
    @return: The C{Swipe} with the id C{id}, or C{None}
    @rtype: L{Swipe}
    """
    return Swipe.query.get(id)

def get_projects_by_pm_id(pm_id):
    """
    Get the projects managed by a PM

    @param pm_id: The PM's id
    @type pm_id: C{int}
    @return: The C{User} with the id C{id}, or C{None}
    @rtype: list of L{Project}
    """
    return Project.query.filter_by(pm_id=pm_id).all()

def get_open_projects(current_state):
    return Project.query.filter_by(current_state=current_state).all()

def get_stack_for_user(user_id):
    """
    Get projects for user to swipe on.

    @param user_id: User's id
    @type user_id: C{int}
    @return: L{Project}s to have the user to swipe on
    @rtype: list of L{Project}
    """
    user_obj = get_user_by_id(user_id)
    return get_projects_with_any_skills(user_obj.skill_sets)

def get_swipes_for(who, id):
    """
    Get a user's swipes as a PM or a developer.
    TODO: implement PM and error checking

    @param who: 0 for pm, 1 for dev.
    @type who: C{int}
    @param id: The id of this person.
    @type id: C{int}
    @return: L{Swipe}s that this person has done
    @rtype: list of L{Swipe}
    """
    swipe_arr = []
    if who == Swipe.SWIPER_DEV:
        swipe_arr = Swipe.query.filter_by(user_id=id).filter_by(who_swiped=Swipe.SWIPER_DEV).all()
    elif who == Swipe.SWIPER_PM:
        #TODO
        swipe_arr = []
    else:
        #TODO error checking
        swipe_arr = []
    return swipe_arr

def get_matches_for(who, id):
    """
    TODO
    Get the matches for an ID. Matches are when both parties have swiped each other.
    @param who: 0 for pm, 1 for dev.
    @type who: C{int}
    @param id: The id of this person.
    @type id: C{int}
    @return: List of IDs of people who we have matched with.
    """

def add_swipe(user_id, project_id, result, who_swiped):
    """
    Add a swipe.
    @see: L{Swipe}
    """
    swipe_obj = Swipe(user_id, project_id, result, who_swiped)
    insert_obj(swipe_obj)
    if result == Swipe.RESULT_NO:
        return None
    complement = {}
    complement[Swipe.SWIPER_DEV] = Swipe.SWIPER_PM
    complement[Swipe.SWIPER_PM] = Swipe.SWIPER_DEV
    complement_swipe = Swipe.query.filter_by(user_id=user_id, project_id=project_id, result=Swipe.RESULT_YES, who_swiped=complement[who_swiped]).first()
    if complement_swipe: #there is a match
        match_obj = Match(user_id, project_id)
        insert_obj(match_obj)
    return complement_swipe

def add_new_user(username, password, first_name=None):
    """
    Creates a new user with blank information
    
    @param username: The username of the new user. Should be distinct
    @type username: C{str}
    @param password: The new user's password
    @type password: C{str}
    @return: User id if user was created, -1 if username already exists
    @rtype: C{int}
    """
    if not first_name:
        first_name = username
    if get_user_by_username(username):
        return -1
    new_user = User(username, first_name, "", "", "")
    add_user_object(new_user, password)
    return get_user_by_username(username).id

def add_new_project(title, description, pm_id):
    """
    Creates a new project with description.
    @param title: The title of the new project
    @type title: C{str}
    @param description: The new project's description
    @type description: C{str}
    @param pm_id: User ID of the project manager that manages this project.
    @type pm_id: C{int}
    @return: Project id if user was created, -1 if project title already exists
             or the pm_id does not exist
    @rtype: C{int}
    """
    if not title or not get_user_by_id(pm_id):
        return -1
    if get_project_by_title(title):
        return -1
    new_project = Project(title, description, pm_id)
    insert_obj(new_project)
    return get_project_by_title(title).id

def add_new_skill(skill_name):
    """
    Creates a new skill with name.

    @param skill_name: The name of the new skill
    @type skill_name: C{str}
    @return Skill id if skill was created, -1 if skill already exists
    @rtype: C{int}
    """
    if not skill_name or get_skill_by_id(skill_name):
        return -1
    new_skill = Skill(skill_name)
    insert_obj(new_skill)
    return get_skill_by_name(skill_name).id

def add_user_object(user_obj, password):
    """
    Creates a new user with a login

    @param user_obj: The user to create
    @type user_obj: L{User}
    @param password: The new user's password
    @type password: C{str}
    """
    insert_obj(user_obj)
    new_login = Login(username=user_obj.username, password=password)
    insert_obj(new_login)

def validate_login(login_obj):
    """
    Checks if the login has a valid username/password combination

    @param login_obj: The C{Login} with the credentials to check
    @type login_obj: L{Login}
    @return: Whether the credentials are valid
    @rtype: C{bool}
    """
    return Login.query.filter_by(username=login_obj.username) \
                      .filter_by(password=login_obj.password) \
                      .first() != None
