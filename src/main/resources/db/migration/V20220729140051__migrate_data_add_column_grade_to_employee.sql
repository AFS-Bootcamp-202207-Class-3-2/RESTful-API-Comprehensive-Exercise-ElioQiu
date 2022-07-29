alter table employee add column grade varchar(255);

update employee set employee.grade='Junior' where salary < 8000;
update employee set employee.grade='Senior' where salary >= 8000;
