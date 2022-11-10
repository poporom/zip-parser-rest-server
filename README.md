zip-parser-rest-server

Interfaces:
1. http://localhost:8081/api/requests/load   - receives zip file, extracts files to temp directory and pars them. 
2. http://localhost:8081/api/requests/result/{id} - shows parsing result by id of parsing request.

Restrictions and settings:
1. Pars regexp is located in parser.properties. It injects in bean, so for changes takes place app should be restarted.
2. File name should be written in eng.
3. New parser should be done in "com/zipparser/util" package. Name must be "Parser" + file extension.



