run:
	python route.py

createdb:
	python database/create_db.py

docs:
	epydoc --html -v .

test:
	python -m unittest discover -v tests

clean:
	rm -f *.pyc **/*.pyc

.PHONY: run createdb docs test clean
