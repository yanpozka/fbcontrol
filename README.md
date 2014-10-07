Facebook Friends Control
========================

Tool to control(with selenium java) of how many friends has one of your 'friends' on facebook.com and 
it send a report by email (gmail), thanks to JavaMail. You need to get a Gmail account.


How to use:
==========
You probably want compile first and get a file like 'ffselenium.jar'

You can add this comand to your crontab or your prefer task schedule:

java -jar ffselenium.jar [email_or_user_fb] [password_fb] [url_to_check_friend] [email_gmail] [passwd_gmail]
