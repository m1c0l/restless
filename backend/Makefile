run:
	python route.py

createdb:
	python database/create_db.py

DOCS_DIR = docs/

docs:
	mkdir -p $(DOCS_DIR)
	pydoc -w ./
	mv *.html $(DOCS_DIR)

clean:
	rm -rf *.pyc **/*.pyc $(DOCS_DIR)

.PHONY: docs
