# CS430 Lab JDBC Application

## Description
This package has two goals:
1. Parsing library csv records into .dump files, to be bulk-loaded into the database.
2. Parsing XML files containing check-out and check-in requests, executing those requests, 
and updating the database accordingly.

## Usage
**Before:** Compile the project: `$ mvn clean && mvn package`

To execute the first goal as stated in the description:
1. `$ cd target/classes/`, and execute **Main**:
   - `$ java Main --dumps`


To execute the second goal as stated in the description:
1. Reset the database back to the state of Lab 3:
   - `$ cd <project>/setup/`
   - Log into the **faure** database, and `$ use <your_database_name>;`
   - Execute **Restart.sql** by running `$ source Restart.sql;`

2. Set the **SQLUSERNAME** and **SQLPASSWORD** environment variables:
   - `$ export SQLUSERNAME=<your_faure_username>`
   - `$ export SQLPASSWORD=<your_faure_password>`
   - Optionally, you may skip this step and provide them at runtime.

3. `$ cd target/classes/`, and execute **Main**:
   - `$ java Main <path_to_request_file>`
   - Or, if you skipped step 2:
       - `$ SQLUSERNAME=<username> SQLPASSWORD=<password> java Main <path_to_request_file>`