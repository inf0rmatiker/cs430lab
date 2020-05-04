# CS430 Lab JDBC Application

## Description
This package has three goals:
1. Parsing library csv records into .dump files, to be bulk-loaded into the database.
2. Parsing XML files containing check-out and check-in requests, executing those requests, 
and updating the database accordingly.
3. Providing a GUI to interact with the library database.

## Prerequisites
**Before using this package for the first time:** 

- Compile the project: `$ mvn clean && mvn package`

- Set the **SQLUSERNAME** and **SQLPASSWORD** environment variables:
   - `$ export SQLUSERNAME=<your_faure_username>`
   - `$ export SQLPASSWORD=<your_faure_password>`
   - Optionally, you may skip this step and provide them at runtime.
   
- Reset the database back to the state of Lab 3:
   - `$ cd <project>/setup/`
   - Log into the **faure** database, and `$ use <your_database_name>;`
   - Execute **Restart.sql** by running `$ source Restart.sql;`

- Reset the database back to the state of Lab 4:
   - Reset back to Lab 3 first
   - `$ cd ./target/classes && java Main -c ./data/Libdata.xml`

## Usage

- Note: A simple way to execute the GUI: `$ ./run.sh`
- Below is the usage message for the Java program:

```
SYNOPSIS:
	java Main ( DUMP_OPT | CHECKOUTS_OPT | GUI_OPT )

DESCRIPTION:

	1. Generating dumps
		Reads all the ./data/*.txt files, and generates SQL dump files into ./dumps/

	2. Executing checkouts
		Reads an xml file specified by the user, containing check-in/check-out records.
		Updates the database accordingly.

	3. Opening a GUI for the user
		Creates a windowed application for the user to check book availability and request info.

OPTIONS:
	DUMP_OPT	-d, --dumps

	CHECKOUTS_OPT	-c, --checkouts XML_FILE
				XML_FILE: A list of activities, in the form:
					<activities>
					  <Borrowed_by>
					    <MemberID> 2011 </MemberID>
					    <ISBN> 96-42103-10800 </ISBN>
					    <Checkout_date> N/A </Checkout_date>
					    <Checkin_date> 06/10/2016 </Checkin_date>
					  </Borrowed_by> 
					  ... 
					</activities>

	GUI_OPT		-g, --gui


```
