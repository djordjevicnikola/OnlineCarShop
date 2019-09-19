package sqlprojekat;

import java.sql.*;
import java.util.Scanner;

public class ProdavnicaA {
	private String connectionString;
	private Connection con;

	public ProdavnicaA(String connectionString) {
		this.connectionString = connectionString;
	}

	public void connect() {
		try {
			con = DriverManager.getConnection(connectionString);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void login() {
		System.out.println("\nUnesite podatke.");
		Scanner sc = new Scanner(System.in);
		System.out.print("  Unesite korisnicko ime: ");
		String korisnickoIme = sc.nextLine();
		String upitKI = "SELECT count(*)\r\n" + "FROM Korisnik\r\n" + "WHERE Username = '" + korisnickoIme + "'";
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(upitKI);
			if (rs.next()) {
				int rez = rs.getInt("count(*)");
				if (rez == 0) {
					System.err.println("  Korisnicko ime nije ispravno.");
					return;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.print("  Unesite lozinku: ");
		String lozinka = sc.nextLine();
		String upitL = "SELECT count(*)\r\n" + "FROM Korisnik\r\n" + "WHERE Username = '" + korisnickoIme
				+ "' AND Password = '" + lozinka + "'";
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(upitL);
			if (rs.next()) {
				int rez = rs.getInt("count(*)");
				if (rez == 0) {
					System.err.println("  Greska prilikom unosa lozinke.");
					return;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\nLogovanje uspesno!");
		System.out.print(
				"\nIzaberite opciju: \n1 - ukoliko zelite da pogledate ponudu\n2 - ukoliko zelite da uplatite ratu\n"
						+ "Unesite vas izbor: ");
		int izbor = sc.nextInt();
		if (izbor == 1) {
			System.out.println("\nU ponudi imamo sledece modele: ");
			this.kupi(korisnickoIme);
		} else if (izbor == 2) {
			System.out.println("\nVasi kupljeni automobili: ");
			this.uplati(korisnickoIme);
		} else {
			System.err.println("Pogresan izbor.");
		}
	}

	public void registracija() {
		Scanner sc = new Scanner(System.in);
		System.out.println("\nNapravite novi nalog!\n");
		System.out.println("Korisnicko ime ne sme sadrzati razmak.");
		System.out.print("  Unesite novo korisnicko ime: ");
		String novoKorisnickoIme = sc.nextLine();
		for (int i = 0; i < novoKorisnickoIme.length(); i++) {
			if (Character.toString(novoKorisnickoIme.charAt(i)).equals(" ")) {
				System.err.println("  Korisnicko ime ne sme sadrzati razmak.");
				return;
			}
		}
		String upitKI = "SELECT count(*)\r\n" + "FROM Korisnik\r\n" + "WHERE Username = '" + novoKorisnickoIme + "'";
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(upitKI);
			if (rs.next()) {
				int rez = rs.getInt("count(*)");
				if (rez == 1) {
					System.err.println("  Korisnicko ime vec postoji.");
					return;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\nLozinka ne sme sadrzati razmak i mora imati vise od 4 karaktera");
		System.out.print("  Unesite novu lozinku: ");
		String novaLozinka = sc.nextLine();
		for (int i = 0; i < novaLozinka.length(); i++) {
			if (novaLozinka.length() < 5) {
				System.err.println("  Lozinka mora da ima vise od 4 karaktera.");
				return;
			} else if (Character.toString(novaLozinka.charAt(i)).equals(" ")) {
				System.err.println("  Lozinka ne sme sadrzati razmak.");
				return;
			}
		}
		System.out.print("  Potvrdite lozinku: ");
		String potvrdaLozinka = sc.nextLine();
		if (!potvrdaLozinka.equals(novaLozinka)) {
			System.err.println("  Lozinke se ne podudaraju.");
			return;
		}

		String insert = "INSERT INTO Korisnik (Username, Password)\r\n" + "VALUES('" + novoKorisnickoIme + "', '"
				+ novaLozinka + "')";
		try {
			Statement stm = con.createStatement();
			stm.executeUpdate(insert);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\nRegistracija uspesna!");
		System.out.print("\nUkoliko zelite da se ulogujete pritisnite 1: ");
		int izbor1 = sc.nextInt();
		if (izbor1 == 1) {
			this.login();
		} else {
			System.err.println("Pogresan unos opcije");
		}
	}

	public void kupi(String korisnickoIme) {
		java.sql.Date datum = new java.sql.Date(System.currentTimeMillis());
		Scanner sc = new Scanner(System.in);
		String upit = "SELECT m.Naziv, a.Cena, a.IdAuto\r\n" + "FROM Automobil a, Model m\r\n"
				+ "WHERE a.IdModel = m.IdModel AND Status = 'n'";
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(upit);
			while (rs.next()) {
				String naziv = rs.getString("Naziv");
				int cena = rs.getInt("Cena");
				int idAuta = rs.getInt("IdAuto");
				System.out.println("- Model: " + naziv + " - Cena: " + cena + " - ID Automobila: " + idAuta);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.print("\nUnesite ID automobila koji zelite da kupite: ");
		int idAuto = sc.nextInt();
		String unos = "INSERT INTO Prodaja (IdAuto, Username, Datum)\r\n" + "VALUES (" + idAuto + ", '" + korisnickoIme
				+ "', '" + datum + "')";
		String nUP = "UPDATE Automobil\r\n" + "SET Status = 'p'\r\n" + "WHERE IdAuto = " + idAuto + "";
		String idModel = "SELECT a.IdModel\r\n" + "FROM Model m, Automobil a\r\n"
				+ "WHERE m.IdModel = a.IdModel AND a.IdAuto = " + idAuto;

		try {
			Statement stm = con.createStatement();
			stm.executeUpdate(unos);
			stm.executeUpdate(nUP);
			ResultSet rs = stm.executeQuery(idModel);
			if (rs.next()) {
				int idModel1 = rs.getInt("IdModel");
				String updateBrPr = "UPDATE Model\r\n" + "SET BrProdatih = BrProdatih + 1\r\n" + "WHERE IdModel = "
						+ idModel1;
				stm.executeUpdate(updateBrPr);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("\nCestitamo! Uspesno kupljen automobil!");
	}

	public void uplati(String korisnickoIme) {
		java.sql.Date datum = new java.sql.Date(System.currentTimeMillis());
		Scanner sc = new Scanner(System.in);
		String upit = "SELECT m.Naziv, a.Cena, p.IdAuto\r\n" + "FROM Prodaja p, Automobil a, Model m\r\n"
				+ "WHERE p.Username = '" + korisnickoIme + "' AND a.IdAuto = p.IdAuto AND m.IdModel = a.IdModel";
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(upit);
			while (rs.next()) {
				String naziv = rs.getString("Naziv");
				int cena = rs.getInt("Cena");
				int idAuta = rs.getInt("IdAuto");
				System.out.println("- Model: " + naziv + " - Cena: " + cena + " - ID Automobila: " + idAuta);
			}
			System.out.print("\nUpisite ID automobila za koji zelite da uplatite novac: ");
			int id = sc.nextInt();
			String ID = "SELECT sum(Iznos)\r\n" + "FROM Uplata\r\n" + "WHERE IdAuto = " + id;
			ResultSet rs1 = stm.executeQuery(ID);
			if (rs1.next()) {
				int doSada = rs1.getInt("sum(Iznos)");
				System.out.println("Do sada uplaceno: " + doSada);
			}
			System.out.print("Upisite koliko zelite da uplatite: ");
			int uplata = sc.nextInt();
			String uplataUpdate = "INSERT INTO Uplata(IdAuto, Iznos,Datum)\r\n" + "VALUES (" + id + ", " + uplata
					+ ", '" + datum + "')";
			stm.executeUpdate(uplataUpdate);
			System.out.println("Uplata uspesno evidentirana!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
