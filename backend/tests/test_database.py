import flask, unittest
import setup
from database.models import *
from database.database import *
from database.db import db

class DatabaseTestCase(unittest.TestCase):
    """
    Unit tests for L{database.database}
    """

    def __init__(self,arg):
        """
        Initializes the test case. Constructs the C{unittest.TestCase} base
        class and creates a Flask app.
        @param arg: The name of the unit test function
        @type arg: C{str}
        """
        super(DatabaseTestCase, self).__init__(arg)
        self.app = setup.create_app()

    def setUp(self):
        """
        Recreate the database.
        """
        db.drop_all()
        db.create_all()

    def tearDown(self):
        """
        Close the database session.
        """
        db.session.remove()

    def test_insert_obj(self):
        """
        Objects inserted to database correctly
        """
        user = User('user123', '', '', '', '')
        insert_obj(user)
        self.assertIsNotNone(user.id)

        project = Project('title', '', user.id)
        insert_obj(project)
        self.assertIsNotNone(project.id)

    def test_update(self):
        """
        Test updating objects
        """
        user = User('user123', '', '', '', 'test bio')
        insert_obj(user)
        changes = {
            'first_name': 'first',
            'last_name': 'last'
        }
        update(user, **changes)
        self.assertEqual(user.first_name, changes['first_name'])
        self.assertEqual(user.last_name, changes['last_name'])
        self.assertEqual(user.bio, 'test bio')
        
        with self.assertRaises(AttributeError):
            update(user, bad_attr=0)

    def test_delete(self):
        """
        Test deleting objects
        """
        user = User('user123', '', '', '', '')
        insert_obj(user)
        project = Project('title', '', user.id)
        insert_obj(project)
        self.assertIsNotNone(get_project_by_id(project.id))
        delete(project)
        self.assertIsNone(get_project_by_id(project.id))

    def test_get_by(self):
        """
        Test methods like C{get_user_by_id} and C{get_skill_by_name}.
        """
        user = User('user123', '', '', '', '')
        insert_obj(user)
        self.assertEqual(get_user_by_username(user.username).id, user.id)
        self.assertEqual(get_user_by_id(user.id).username, user.username)

        project = Project('title', '', user.id)
        insert_obj(project)
        self.assertEqual(get_project_by_title(project.title).id, project.id)
        self.assertEqual(get_project_by_id(project.id).title, project.title)
        self.assertIn(project, get_projects_by_pm_id(user.id))

        skill = Skill('Python')
        insert_obj(skill)
        self.assertEqual(get_skill_by_name(skill.skill_name).id, skill.id)
        self.assertEqual(get_skill_by_id(skill.id).skill_name, skill.skill_name)

        swipe = Swipe(user.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        insert_obj(swipe)
        self.assertEqual(get_swipe_by_id(swipe.id).user_id, swipe.user_id)
        self.assertEqual(get_swipe_by_id(swipe.id).result, swipe.result)

    def test_add_swipe(self):
        """
        Test adding a swipe.
        """
        user = User('user1', '', '', '', '')
        insert_obj(user)
        project = Project('title', '', user.id)
        insert_obj(project)

        comp = add_swipe(user.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        self.assertIsNone(comp)
        comp = add_swipe(user.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_PM)
        self.assertEqual(comp.user_id, user.id)
        self.assertEqual(comp.project_id, project.id)
        self.assertEqual(comp.result, Swipe.RESULT_YES)
        self.assertEqual(comp.who_swiped, Swipe.SWIPER_DEV)

        user2 = User('user2', '', '', '', '')
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        self.assertIsNone(comp)
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_NO, Swipe.SWIPER_PM)
        self.assertIsNone(comp)

        user3 = User('user3', '', '', '', '')
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_NO, Swipe.SWIPER_PM)
        self.assertIsNone(comp)
        comp = add_swipe(user2.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        self.assertIsNone(comp)

    def test_get_user_swipes(self):
        """
        Test getting a user's swipes.
        TODO: get PM swipes
        """
        user = User('user1', '', '', '', '')
        insert_obj(user)
        user2 = User('user2', '', '', '', '')
        insert_obj(user2)
        project = Project('title', '', user.id)
        insert_obj(project)

        add_swipe(user2.id, project.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)
        add_swipe(user2.id, project.id, Swipe.RESULT_NO, Swipe.SWIPER_PM)
        dev_swipes = get_swipes_for(Swipe.SWIPER_DEV, user2.id)
        self.assertEqual(len(dev_swipes), 1)
        self.assertEqual(dev_swipes[0].who_swiped, Swipe.SWIPER_DEV)

    def test_get_projects_with_skills(self):
        """
        Test getting projects that match any of a number of skills.
        """
        skill = Skill('s1')
        insert_obj(skill)
        skill2 = Skill('s2')
        insert_obj(skill2)
        user = User('user1', '', '', '', '')
        insert_obj(user)
        project = Project('title', '', user.id)
        project.skills_needed.append(skill)
        insert_obj(project)
        project2 = Project('title2', '', user.id)
        project2.skills_needed.append(skill2)
        insert_obj(project2)
        project3 = Project('title3', '', user.id)
        project3.skills_needed.append(skill)
        project3.skills_needed.append(skill2)
        insert_obj(project3)

        skill_arr = [skill]
        proj_set = get_projects_with_any_skills(skill_arr)
        self.assertEqual(len(proj_set), 2)
        self.assertIn(project, proj_set)
        self.assertIn(project3, proj_set)

        skill_arr = [skill, skill2]
        proj_set = get_projects_with_any_skills(skill_arr)
        self.assertEqual(len(proj_set), 3)

        skill_arr = []
        proj_set = get_projects_with_any_skills(skill_arr)
        self.assertEqual(len(proj_set), 0)

    def test_get_user_stack(self):
        """
        Test getting a user's stack.
        """
        skill = Skill('s1')
        insert_obj(skill)
        user = User('user1', '', '', '', '')
        user.skill_sets.append(skill)
        insert_obj(user)
        user2 = User('user2', '', '', '', '')
        insert_obj(user2)
        project = Project('title', '', user.id)
        project.skills_needed.append(skill)
        project.skill_weights.append(Weighted_Skill(project.id, skill.id, 5.0))
        insert_obj(project)
        project2 = Project('title2', '', user2.id)
        project2.skills_needed.append(skill)
        project2.skill_weights.append(Weighted_Skill(project2.id, skill.id, 5.0))
        insert_obj(project2)
        project3 = Project('title3', '', user2.id)
        project3.skills_needed.append(skill)
        project3.skill_weights.append(Weighted_Skill(project3.id, skill.id, 5.0))
        insert_obj(project3)
        add_swipe(user.id, project2.id, Swipe.RESULT_YES, Swipe.SWIPER_DEV)

        #explanation: user1 has 1 skill that matches all 3 projects,
        #but user1 owns first project and has swiped on project2 so the only
        #thing in the stack should be project 3
        stack = get_stack_for_user(user.id)
        self.assertEqual(len(stack), 1)
        self.assertEqual(stack[0].title, 'title3')

        #user4 has the skill that matches all 3 projects but has a higher
        #desired salary than their pay rate (which is 0 by default)
        user4 = User('user4', '', '', '', '')
        user4.skill_sets.append(skill)
        user4.desired_salary = 100
        insert_obj(user4)
        stack = get_stack_for_user(user4.id)
        self.assertEqual(len(stack), 0)

        #normal case: user5 has skill matching 3 projects
        user5 = User('user5', '', '', '', '')
        user5.skill_sets.append(skill)
        insert_obj(user5)
        stack = get_stack_for_user(user5.id)
        self.assertEqual(len(stack), 3)

        # bad id
        with self.assertRaises(ValueError):
            get_stack_for_user(-1)

    def test_add_new_user(self):
        """
        Test adding a user.
        """
        id = add_new_user('user1', 'pass123')
        self.assertGreater(id, 0)
        id = add_new_user('user1', 'otherpass')
        self.assertEqual(id, -1)
        id = add_new_user('user2', 'pass', 'firstname')
        self.assertEqual('firstname', get_user_by_id(id).first_name)

    def test_add_new_project(self):
        """
        Test adding a project.
        """
        user_id = add_new_user('user1', 'pass')
        self.assertEqual(add_new_project(None, '', user_id), -1)
        self.assertEqual(add_new_project('p1', '', 0), -1)
        project_id = add_new_project('p1', '', user_id)
        project = get_project_by_id(project_id)
        self.assertEqual(project.title, 'p1')

    def test_add_new_skill(self):
        """
        Test adding a skill.
        """
        id = add_new_skill('s1')
        self.assertGreater(id, 0)
        id = add_new_skill(None)
        self.assertEqual(id, -1)

    def test_login(self):
        """
        Test adding and validating logins
        """
        user = User('user1', '', '', '', '')
        add_user_object(user, 'pass123')
        self.assertTrue(validate_login(Login('user1', 'pass123')))
        self.assertFalse(validate_login(Login('user1', 'wrong pass')))
        self.assertFalse(validate_login(Login('wrong user', 'pass123')))
