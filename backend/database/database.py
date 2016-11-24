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
    @rtype: L{Project}
    """
    return Project.query.filter_by(title=title).first()

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

def get_swipes_for(who, id):
    """
    currently unused, TODO
    """
    pass    
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
    return Swipe(user_id, project_id, result, who_swiped)

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
    @rtype: C{int}
    """
    if not title or not descripion or not pm_id:
        return -1
    if get_project_by_title(title):
        return -1
    new_project = Project(title, description, pm_id)
    insert_obj(new_project)
    return get_project_by_title(title).id

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
