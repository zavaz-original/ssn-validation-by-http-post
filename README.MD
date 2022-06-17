Virnex Oy / juha valimaki 2022-06-13

# User Guide for SSN/PIC Validation App

### The validation intelligence is in the end of PicFin.java under sb_juhav/siili_pic/validation/

The smart part is the following method in the end of PicFin.java:
public boolean isOfValidFormat(String pic)

There the ssn is sent into a java8+ Stream with a chain of several filter & lambda.
Only a valid ssn will pop out from the end of the chain.
If the ssn is invalid, an Optional Empty will pop out.

### Note: the name siili will be removed from the java/Spring Boot source codes

There is a company named Siili Solutions Oyj and their web site is siili.com, but this App is NOT from the Siili Solutions Oyj / Siili.com

I will handle the renaming in the software ASAP and update Github accordingly.


Additionally in the next version I will update both the project file structure and the naming of java packages and methods. The current version was written in a hurry to just get the algorithms working.
Especially I don't like my current project tree structure and naming here. I would redesign and rewrite it but I had a lack of time.

It is a Java/Spring Boot Application.

## What do SSN and PIC mean?
PIC stands for Personal Identity Code. A unique identity code (string of characters) for each registered individual in a country. The format is country specific.

SSN stands for Social Security Number - the PIC used in Finland.


## For test open e.g. your POSTMAN App and follow:

(I recommend to jump directly over to the next step "What should you see...")

1. Select method as POST from the drop down menu up in the left horizontal gray area.

2. Type the URL like below next to the POST text you selected: http://localhost:8080/validate_ssn

3. Select 'Body' on the next line.

4. Select 'raw' on the next line.

5. Type the contents of the Body in JSON format in the big white area next down:  
{ “country” : “FIN”,   “ssn” : "131052-308T" }

6. Click the BIG BLUE Send button on the right hand side.

7. The ssn validation result will pop up on the left most down: 	true

8. To test an invalid case change the last character of ssn value from T to K and click Send:	false

9. Change the K back to the valid T

10. Change the value of country from “FIN” to “USA” and click Send:	false

# What should you see on your POSTMAN window?

## A positive test case with result true:

	POST	http://localhost:8080/validate_ssn
	
	Body
	
	raw
	
	{“country” : “FIN”,  “ssn” : "131052-308T" }
	
---
	
	true
	
	The true pops up most down after clicking send.
	
## Modify to test negative cases with response false:

{“country” : “FIN”,  “ssn” : "131052-308K" }

---

false 		

(the K instead of T in the end of the ssn makes it invalid)

or

{“country” : “USA”,  “ssn” : "131052-308T" }

---

false

(because the valid Finnish (FIN) ssn is an invalid pic in the USA)

That's all for now.

