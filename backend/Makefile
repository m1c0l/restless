run:
	python route.py

createdb:
	python database/create_db.py

docs:
	epydoc --html -v .

test:
	python -m unittest discover -v tests

.coverage: tests/*.py
	coverage run -m unittest discover -v tests

coverage: .coverage
	@#coverage html **/*.py
	coverage report -m **/*.py

clean:
	rm -f *.pyc **/*.pyc .coverage

.PHONY: run createdb docs test coverage clean
