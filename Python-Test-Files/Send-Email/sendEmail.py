import smtplib
import config
from datetime import datetime
subject = "**** Security Alert ****"
msg = "Warning! We have detected movement around your house. "

def send_email(subject, msg):
    try:
        DateTime = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        server = smtplib.SMTP('smtp.gmail.com:587')
        server.ehlo()
        server.starttls()
        server.login(config.EMAIL_ADDRESS, config.PASSWORD)
        msg = msg + "\nEvent Occured: " + DateTime
        message = 'Subject: {}\n\n{}'.format(subject, msg)
        server.sendmail(config.EMAIL_ADDRESS, config.EMAIL_ADDRESS, message)
        server.quit()
        print("Success: Email sent!")
        #print(DateTime)

    except:
        print("Email Failed to send.")

send_email(subject, msg)

