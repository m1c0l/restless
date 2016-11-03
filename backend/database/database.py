from db import db
from user import User 
from project import Project
from skill import Skill
from devs_to_projects import devs_to_projects
from project_skills import project_skills
from user_skills import user_skills
from swipe import Swipe
from login import Login

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


# TODO: use *_or_404 for stuff

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

def get_skill_by_id(id):
    """
    Get a Skill by id

    @param id: The skill's id
    @type id: C{int}
    @return: The C{Skill} with the id C{id}, or C{None}
    @rtype: L{Skill}
    """
    return Skill.query.get(id)

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

def add_new_user(user_obj, password):
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
