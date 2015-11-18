package axmlDoc.samples;

import java.io.File;
import java.util.Scanner;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import axmlDoc.AxmlDoc;
import axmlDoc.AxmlDoc.HandleStrategy;
import axmlDoc.AxmlDoc.ReturnType;

public class AxmlDocDesktopClient {

	public static void main(String args[]) {
			System.out.println("AXMLDoc Lib demo ===================================");
			callMainMenu();

	}
	
	private static void callMainMenu(){
		Scanner input;
		Boolean flagContinue = false;

		while (!flagContinue) {
			System.out.println("Main menu - choose an option: ");				
			System.out.println("1 - Read a book");
			System.out.println("2 - Reset materialized data (book file will be reset)");
			input = new Scanner(System.in);
			String choosenOption = input.nextLine();

			switch (choosenOption) {
			case "1":
				readBook();
				flagContinue = true;
				break;
			case "2":
				resetMaterializedData();
				System.out.println("Data reverted. Original file is with minimum size.");
				break;
			default:
				System.out.println("Invalid option.");
				break;
			}
		}
	}

	private static void resetMaterializedData() {
		File fXmlFile = new File("livro.xml");
		AxmlDoc axmlDoc = new AxmlDoc(fXmlFile, HandleStrategy.LAZY_PERSIST_WITH_EXCLUSION, ReturnType.TEXT);
		axmlDoc.revertMaterializedData();
		axmlDoc.persist();
		
	}

	private static void readBook() {
		HandleStrategy strategy = null;
		File fXmlFile = new File("livro.xml");
		Long originalSize = fXmlFile.length();

		while (strategy == null) {
			System.out.println("Choose a strategy for data materialization: ");
			System.out.println("1 - LAZY");
			System.out.println("2 - LAZY_PERSIST");
			System.out.println("3 - LAZY_PERSIST_WITH_EXCLUSION");
			System.out.println("4 - EAGER");
			System.out.println("5 - EAGER_PERSIST");
			Scanner input = new Scanner(System.in);
			String choosenStrategy = input.nextLine();

			switch (choosenStrategy) {
			case "1":
				strategy = HandleStrategy.LAZY;
				input.close();
				break;
			case "2":
				strategy = HandleStrategy.LAZY_PERSIST;
				input.close();
				break;
			case "3":
				strategy = HandleStrategy.LAZY_PERSIST_WITH_EXCLUSION;
				input.close();
				break;
			case "4":
				strategy = HandleStrategy.EAGER;
				input.close();
				break;
			case "5":
				strategy = HandleStrategy.EAGER_PERSIST;
				input.close();
				break;
			default:
				System.out.println("Invalid option.");
			}
		}

		AxmlDoc axmlDoc = new AxmlDoc(fXmlFile, strategy, ReturnType.TEXT);

		axmlDoc.getDocumentElement().normalize();

		NodeList nList = axmlDoc.getElementsByTagName("livro");

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;

				// Exibe nome do Livro
				System.out.println("Example book title: " + eElement.getAttribute("titulo"));
				System.out.println("File size: " + originalSize);

			}

			// Iniciando simulação de uso do usuário

			boolean flagExit = false;

			while (!flagExit) {
				System.out.println("Choose a chapter (number only, 0 for main menu.):");
				// Exibe lista de capítulos do livro
				NodeList capitulos = nNode.getChildNodes();
				for (int c = 0; c < capitulos.getLength(); c++) {
					Node capituloNode = capitulos.item(c);
					if (capituloNode.getNodeType() == Node.ELEMENT_NODE) {
						Element capitulo = (Element) capituloNode;
						System.out.println("Capitulo " + capitulo.getAttribute("numero") + ": "
								+ capitulo.getAttribute("titulo"));
					}
				}

				Scanner input = new Scanner(System.in);
				String choosenChapter = input.nextLine();

				switch (choosenChapter) {
				case "1":
					System.out.println("Chapter 1 contents:");
					System.out.println(axmlDoc.getTextFromSingleNode("/livro/capitulo[1]"));
					System.out.println("");
					System.out.println("Actual book file size: " + fXmlFile.length() + "B. Was "
							+ originalSize + "B.");
					System.out.println("ps.: See the original XML file to check strategy effects.");
					input.close();
					break;
				case "2":
					System.out.println("Chapter 2 contents:");
					System.out.println(axmlDoc.getTextFromSingleNode("/livro/capitulo[2]"));
					System.out.println("");
					System.out.println("Actual book file size: " + fXmlFile.length() + "B. Was "
							+ originalSize + "B.");
					System.out.println("ps.: See the original XML file to check strategy effects.");
					input.close();
					break;
				case "3":
					System.out.println("Chapter 3 contents:");
					System.out.println(axmlDoc.getTextFromSingleNode("/livro/capitulo[3]"));
					System.out.println("");
					System.out.println("Actual book file size: " + fXmlFile.length() + "B. Was "
							+ originalSize + "B.");
					System.out.println("ps.: See the original XML file to check strategy effects.");
					input.close();
					break;
				case "0":
					flagExit = true;
					break;
				default:
					System.out.println("Invalid option.");
					break;
				}
			}
			callMainMenu();
		}

	}
}