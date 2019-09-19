package sqlprojekat;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Dobrodosli u prodavnicu automobila!\n");
		ProdavnicaA prA = new ProdavnicaA("jdbc:sqlite:C:\\Users\\User\\Desktop\\Prodavnica.db");
		prA.connect();
		System.out
				.print("Izaberite opciju: \n1 - ukoliko zelite da se ulogujete\n2 - ukoliko zelite da se registrujete\n"
						+ "Unesite vas izbor: ");
		int izbor = sc.nextInt();
		if (izbor == 1) {
			prA.login();
		} else if (izbor == 2) {
			prA.registracija();
		} else {
			System.err.println("Pogresan unos opcije");
		}

		prA.disconnect();
	}

}
