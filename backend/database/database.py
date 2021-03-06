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
    try:
        db.session.add(obj)
        db.session.commit()
    except: # IntegrityError
        db.session.rollback()

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
    try:
        db.session.commit()
    except:
        db.session.rollback()

def delete(obj):
    """
    Deletes an object from the database

    @param obj: The existing object in the database to be deleted
    @type obj: flask_sqlalchemy.Model
    """
    try:
        db.session.delete(obj)
        db.session.commit()
    except:
        db.session.rollback()

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
    @rtype: set of L{Project}
    """
    #I know, the below code is really silly but I can't seem to query relationships
    #properly with an in_ function...
    #I would've tried querying the user_skills table directly but our code isn't set up to do that
    skill_ids = []
    project_set = set()
    for skill in skill_list:
        #Get all projects with a specific skill, append to list of projects
        proj = set(Project.query.filter(Project.skills_needed.any(id=skill.id)).all())
        #merge sets to remove duplicates
        project_set = project_set.union(proj)
    return project_set
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

def get_login_by_user_id(id):
    """
    Gets the login info for a user
    @param id: The User's id
    @type id: C{int}
    @return: The User's user/pass
    @rtype: C{Login}
    @see: L{Login}
    """
    try:
        user = get_user_by_id(id)
        return Login.query.filter_by(username=user.username).first()
    except AttributeError:
        return None

def get_open_projects(current_state):
    """
    Gets a list of projects with the current state.
    @param current_state: the desired state of the project.
    @type current_state: C{int}
    @return: List of projects with this state.
    @rtype: C{Project}
    """
    return Project.query.filter_by(current_state=current_state).all()

def get_stack_for_user(user_id):
    """
    Get projects for user to swipe on.

    @param user_id: User's id
    @type user_id: C{int}
    @return: L{Project}s to have the user to swipe on
    @rtype: list of L{Project}
    @raise ValueError: If C{user_id} isn't a valid user id
    """
    user_obj = get_user_by_id(user_id)
    if not user_obj:
        raise ValueError("Invalid user ID: %d" % user_id)
    #start the stack with projects with skills that match any of user's skills
    stack = get_projects_with_any_skills(user_obj.skill_sets)
    #subtract the projects the user is PM on
    user_pm_projects = set(user_obj.projects_managing)
    stack = stack.difference(user_pm_projects)
    #keep projects in recruiting state that have higher pay rate than desired by user
    #me trying to be pythonic
    stack = [proj for proj in stack if
        proj.current_state == Project.STATE_RECRUITING and
        proj.pay_range >= user_obj.desired_salary]
    #subtract any projects the user has swiped on as developer
    user_dev_swipes = get_swipes_for(Swipe.GET_USER_SWIPES, user_id, Swipe.SWIPER_DEV)
    #me trying to be pythonic
    user_swiped_proj_ids = [s.project_id for s in user_dev_swipes]
    stack = [proj for proj in stack if not proj.id in user_swiped_proj_ids]
    user_skill_ids = [s.id for s in user_obj.skill_sets]
    #for each stack project, calculate a score that ranks the project in the stack

    #big wage/hr to divide salaries against
    MAX_PAY = 250.0
    #what percentage the pay contributes to the score
    PAY_WEIGHT_IN_SCORE = 0.4
    #what percentage the matched skill contributes to the score
    SKILL_WEIGHT_IN_SCORE = 0.6
    project_score_dict = dict()
    for proj in stack:
        total_proj_weight = 0.0
        user_matched_weight = 0.0
        #see how many skills the user matches and calculate what % of the total
        #project weights the user satisfies
        for skill_and_weight in proj.skill_weights:
            total_proj_weight += skill_and_weight.skill_weight
            if skill_and_weight.skill_id in user_skill_ids:
                user_matched_weight += skill_and_weight.skill_weight
        if total_proj_weight == 0.0:
            raise ValueError("Project %d: No skills or skill weights sum to 0!" % proj.id)
        user_skill_match_percent = user_matched_weight / total_proj_weight
        #calculate the weight that the project's pay satisfies
        proj_pay_match_percent = proj.pay_range / MAX_PAY
        proj_score = PAY_WEIGHT_IN_SCORE * proj_pay_match_percent + SKILL_WEIGHT_IN_SCORE * user_skill_match_percent
        project_score_dict[proj] = proj_score

    project_score_list = project_score_dict.items()
    #sort by project score descending
    project_score_list.sort(key=lambda proj_score: proj_score[1], reverse=True)
    print "project scores for stack", project_score_list
    stack = [proj_score[0] for proj_score in project_score_list]
    return stack
    #old code: for each stack project, find what % of its needed skills the user has
        #num_skills_needed = len(proj.skills_needed)
        #matching_skills = set(user_obj.skill_sets).intersection(set(proj.skills_needed))
        #num_matching_skills = len(matching_skills)
        #percent_matching = float(num_matching_skills) / num_skills_needed
        #skill_matching_dict[proj] = percent_matching

def get_stack_for_project(project_id):
    """
    Get users for project PM to swipe on.

    @param project_id: Project's id
    @type project_id: C{int}
    @return: L{User}s to have the project PM to swipe on
    @rtype: list of L{User}
    @raise ValueError: If C{project_id} isn't a valid project id
    """

    project_obj = get_project_by_id(project_id)
    if not project_obj:
        raise ValueError("Invalid project id: %d" % project_id)
    #first get all the swipes of users who've swiped on this project
    dev_swipes = get_swipes_for(Swipe.GET_PROJECT_SWIPES, project_id, Swipe.SWIPER_DEV)
    #set stack to the users who swiped yes
    stack = {swipe.user_id for swipe in dev_swipes if swipe.result == Swipe.RESULT_YES}
    #subtract users that project PM has swiped on
    pm_swipes = Match.query.filter_by(project_id=project_id).all()
    pm_swipes_user_ids = {match.user_id for match in pm_swipes}
    print "pm swipe user ids", pm_swipes_user_ids
    #old implementation not using Match
    #pm_swipes = get_swipes_for(Swipe.GET_PROJECT_SWIPES, project_id, Swipe.SWIPER_PM)
    #pm_swipes_user_ids = {swipe.user_id for swipe in pm_swipes}
    stack = list(stack.difference(pm_swipes_user_ids))
    stack = [get_user_by_id(id) for id in stack]
    #calculate a score for each user to rank them by
    user_score_dict = dict()
    for user in stack:
        total_proj_weight = 0.0
        user_matched_weight = 0.0
        user_skill_ids = [skill.id for skill in user.skill_sets]
        #see how many skills the user matches and calculate what % of the total
        #project weights the user satisfies
        for skill_and_weight in project_obj.skill_weights:
            total_proj_weight += skill_and_weight.skill_weight
            if skill_and_weight.skill_id in user_skill_ids:
                user_matched_weight += skill_and_weight.skill_weight
        if total_proj_weight == 0.0:
            raise ValueError("Project %d: No skills or skill weights sum to 0!" % proj.id)
        user_skill_match_percent = user_matched_weight / total_proj_weight
        user_score_dict[user] = user_skill_match_percent
    user_score_list = user_score_dict.items()
    #sort by user score descending
    user_score_list.sort(key=lambda user_score: user_score[1], reverse=True)
    print "user scores:", user_score_list
    stack = [user_score[0] for user_score in user_score_list]
    return stack

def get_swipes_for(who, id, who_swiped):
    """
    Get swipes for a user or project.

    @param who: Swipe.GET_PROJECT_SWIPES for project, Swipe.GET_USER_SWIPES for user.
    @type who: C{int}
    @param id: The id of this obejct.
    @type id: C{int}
    @param who_swiped: Swipe.SWIPER_PM for PM or Swipe.SWIPER_DEV for developer
    @type who_swiped: C{int}
    @return: L{Swipe}s that this user/project has done
    @rtype: list of L{Swipe}
    @raise ValueError: if who isn't Swipe.GET_USER_SWIPES or Swipe.GET_PROJECT_SWIPES
    """
    swipe_arr = []
    if who == Swipe.GET_USER_SWIPES:
        swipe_arr = Swipe.query.filter_by(user_id=id).filter_by(who_swiped=who_swiped).all()
    elif who == Swipe.GET_PROJECT_SWIPES:
        swipe_arr = Swipe.query.filter_by(project_id=id).filter_by(who_swiped=who_swiped).all()
    else:
        raise ValueError("%d isn't Swipe.GET_USER_SWIPES or Swipe.GET_PROJECT_SCORES" % who)
    return swipe_arr

def get_matches_for(who, id, type):
    """
    Get the matches for an ID. Matches are when both parties have swiped each other.
    @param who: 0 for project, 1 for dev.
    @type who: C{int}
    @param id: The id of this person or project.
    @type id: C{int}
    @param type: The type of match. 0 means match declined, 1 means match made, 2 means match accepted.
    @type type: C{int}
    @return: List of IDs of people or projects who we have matched with, with the certain type.
    @rtype: list of L{int}
    """
    if who == 0:
        return Match.query.filter_by(project_id=id, result=type).all()
    else:
        return Match.query.filter_by(user_id=id, result=type).all()

def update_match(user_id, project_id, new_result=None):
    """
    Update a match, changing the result of the match to new_result, or incrementing if not given.
    @param user_id: The id of the user in this match.
    @param project_id: The id of the project in this match.
    @param new_result: The new result of this match.
    @type user_id: C{int}
    @type project_id: C{int}
    @type new_result: C{int}
    @return: The new result of this match, 0 if the match had been previously declined, or -1 if the match was not found.
    @rtype: C{int}
    """
    match_obj = Match.query.filter_by(user_id=user_id,project_id=project_id).first()
    if not match_obj:
        return -1
    if new_result == None:
        new_result = match_obj.result + 1
    if match_obj.result == 0:
        return 0
    update(match_obj, result = new_result)
    if new_result == 3: #both user and PM have accepted the match. add to list of confirmed devs
        user_obj = get_user_by_id(user_id)
        project_obj = get_project_by_id(project_id)
        if not user_obj or not project_obj:
            return -1
        user_projects = user_obj.projects_developing
        if project_obj not in user_projects:
            user_projects.append(project_obj)
            update(user_obj, projects_developing = user_projects)
        pm_obj = get_user_by_id(project_obj.pm_id)
        pm_projects = pm_obj.projects_managing
        if project_obj not in pm_projects:
            pm_projects.append(project_obj)
            update(pm_obj, projects_managing = pm_projects)
    if new_result == 0: #someone has declined the match. remove from all lists
        user_obj = get_user_by_id(user_id)
        project_obj = get_project_by_id(project_id)
        user_projects = user_obj.projects_developing
        if project_obj in user_projects:
            user_projects.remove(project_obj)
            update(user_obj, projects_developing = user_projects)
        pm_obj = get_user_by_id(project_obj.pm_id)
        pm_projects = pm_obj.projects_managing
        if project_obj in pm_projects:
            pm_projects.remove(project_obj)
            update(pm_obj, projects_managing = pm_projects)
    return new_result

def delete_pending_matches_with_project_id(project_id):
    """
    Delete all pending matches with a certain project id. This occurs when the project has entered
    a starting state, or is no longer taking new users. This will not delete any confirmed matches,
    but only matches with result <= 1.
    @param project_id: The project ID.
    @type project_id: C{int}
    """
    projects_to_delete = Match.query.filter(Match.project_id==project_id, Match.result <= 1)
    for project_to_delete in projects_to_delete:
        db.session.delete(project_to_delete)
    db.session.commit()

def add_swipe(user_id, project_id, result, who_swiped):
    """
    Add a swipe.
    @param result: one of C{Swipe.RESULT_NO} or C{Swipe.RESULT_YES}
    @param who_swiped: one of C{Swipe.SWIPER_DEV} or C{Swipe.SWIPER_PM}
    @see: L{Swipe}
    """
    try:
        swipe_obj = Swipe(user_id, project_id, result, who_swiped)
        insert_obj(swipe_obj)
    except Exception:
        return None

    if result != Swipe.RESULT_YES:
        return None

    if who_swiped == Swipe.SWIPER_DEV:
        complement = Swipe.SWIPER_PM
    elif who_swiped == Swipe.SWIPER_PM:
        complement = Swipe.SWIPER_DEV
    else:
        return None

    complement_swipe = Swipe.query.filter_by(user_id=user_id,
                                             project_id=project_id,
                                             result=Swipe.RESULT_YES,
                                             who_swiped=complement).first()
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

def get_confirmed_devs(proj_id):
    """
    Returns the list of users who are developers on this project
    @param proj_id: The project's id
    @type proj_id: C{int}
    @return: the list of users who are developers on this project
    @rtype: list of L{User}
    """
    try:
        p = get_project_by_id(proj_id)
        return User.query.filter(User.projects_developing.contains(p)).all()
    except AttributeError:
        return None
