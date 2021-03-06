import flask
import unittest
import setup
from database.models import Skill
from database.db import db

class SkillTestCase(unittest.TestCase):
    """
    Unit tests for L{Skill}
    """

    def __init__(self,arg):
        """
        Initializes the test case. Constructs the C{unittest.TestCase} base
        class and creates a Flask app.
        @param arg: The name of the unit test function
        @type arg: C{str}
        """
        super(SkillTestCase, self).__init__(arg)
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

    def test_init(self):
        """
        The Skill is constructed correctly.
        """
        attrs = {
            'skill_name': 'Skill'
        }
        skill = Skill(**attrs)
        for attr, value in attrs.iteritems():
            self.assertEqual(getattr(skill, attr), value)

    def test_id_unique(self):
        """
        Two skills should have unique id's.
        """
        s1 = Skill('Skill 1')
        s2 = Skill('Skill 2')
        db.session.add(s1)
        db.session.add(s2)
        db.session.commit()
        self.assertNotEqual(s1.id, s2.id)
        
    def test_name_not_nullable(self):
        """
        A Skill's name is not nullable.
        """
        db.session.add(Skill(None))
        with self.assertRaisesRegexp(Exception, 'OperationalError'):
            db.session.commit()

    def test_name_unique(self):
        """
        The Skill's name must be unique.
        """
        db.session.add(Skill('Skill'))
        db.session.add(Skill('Skill'))
        with self.assertRaisesRegexp(Exception, 'IntegrityError'):
            db.session.commit()

    def test_insert_db(self):
        """
        The Skills are inserted into the database correctly.
        """
        names = [str(i) for i in range(10)]
        for name in names:
            db.session.add(Skill(name))
        db.session.commit()

        # all skills are inserted
        skills = Skill.query.all()
        self.assertEqual(len(names), len(skills))

        # check the skill names
        for name in names:
            q = Skill.query.filter_by(skill_name=name).all()
            self.assertEqual(len(q), 1)

    def test_serializable(self):
        """
        A Skill can be represented as JSON.
        """
        s = Skill('Skill')
        db.session.add(s)
        db.session.commit()
        with self.app.test_request_context():
            flask.jsonify(**s.to_dict())
