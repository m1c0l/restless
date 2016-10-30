from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()

#@app.teardown_appcontext
#def session_shutdown(exception=None):
#    db_session.remove()
