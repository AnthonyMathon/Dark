import java.io.IOException;

public class MainForMailing {
	
	//main
	public static void main(String[] args) {
		String mailtype;
		String mail;
		String name;
		if (args.length != 3){
			mail = "anthony.mathon@etudiant.univ-lille1.fr";
			name = "Anthony";
			mailtype = "confirm";
			System.out.println("Default settings set. To use proper settings, use args like this :\nmail name type\n mail = adress to send\n name=name of dest\n type=(confirm/warning/blocking) (default = confirm)");
		}else{
			mail = args[0];
			name = args[1];
			mailtype = args[2];
		}
		
		switch (mailtype) {
		case "confirm":
			try {
				System.out.println("Tentative d'envoi de mail de confirmation a " + name + " via l'adresse " +mail);
				Mailing.sendConfirmMail(mail, name);
			} catch (IOException e) {
				System.out.println("Erreur d'envoi de mail de confirmation");
				e.printStackTrace();
			}
			break;
		case "warning":
			try {
				System.out.println("Tentative d'envoi de mail d'avertissement a " + name + " via l'adresse " +mail);
				Mailing.sendWarningMail(mail, name);
			} catch (IOException e) {
				System.out.println("Erreur d'envoi de mail d'avertissement");
				e.printStackTrace();
			}
			break;
		case "blocking":
			try {
				System.out.println("Tentative d'envoi de mail de blocage a " + name + " via l'adresse " +mail);
				Mailing.sendBlockingMail(mail, name);
			} catch (IOException e) {
				System.out.println("Erreur d'envoi de mail de blocage");
				e.printStackTrace();
			}
			break;
		default: //cas par defaut : confirm
			try {
				System.out.println("Tentative d'envoi de mail de confirmation a " + name + " via l'adresse " +mail);
				Mailing.sendConfirmMail(mail, name);
			} catch (IOException e) {
				System.out.println("Erreur d'envoi de mail de confirmation");
				e.printStackTrace();
			}
			break;
		}
		
	}

}