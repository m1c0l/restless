run:
	python route.py

createdb:
	python database/create_db.py

docs: *.py **/*.py
	pydoc -w ./
	mv *.html docs/

clean:
	rm -f *.pyc **/*.pyc docs/
