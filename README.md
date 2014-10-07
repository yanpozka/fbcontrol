Facebook Friends Control
========================

Tool to control (with selenium java http://www.seleniumhq.org) of how many friends has one of your 'friends' on facebook.com and it send a report by email (gmail), thanks to JavaMail http://www.oracle.com/technetwork/java/javamail/index.html
https://javamail.java.net/nonav/docs/api/

You need have a Gmail account.


How to use:
==========
First you may want to compile and generate a file like 'ffselenium.jar' (for example)

You can add this comand to your crontab or your prefer task schedule:

java -jar ffselenium.jar [email_or_user_fb] [password_fb] [url_to_check_friend] [email_gmail] [passwd_gmail]
