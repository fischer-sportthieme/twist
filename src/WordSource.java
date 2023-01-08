import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordSource {
	public static final String FILENAME = "woerterliste.txt";
	public static final String CACHEFILENAME = "sortiertewoerterliste.txt";
	
	public static List<String> getWordList() {
		List<String> stringList = new ArrayList<String>();
		String fileAsString = "";
		String md5Hash = "";
		try {
			fileAsString = Files.readString(Paths.get(FILENAME));
			md5Hash = getWordListMd5Hash(fileAsString);
		} catch (IOException ioe) {
			System.out.printf("ERROR: Unable to read word list, see stack trace below:%n%n");
			ioe.printStackTrace();
			System.exit(2);
		}

		try {
			stringList = Files.readAllLines(Paths.get(CACHEFILENAME));
			String metadataString = "";
			do {
				metadataString = stringList.remove(stringList.size() - 1);
			} while (Objects.equals(metadataString, ""));
			CachedWordListMetadata metadata = CachedWordListMetadata.fromCsv(metadataString);
			if (Objects.equals(metadata.getWordListMd5Hash(), md5Hash)) {
				return stringList;
			}
		} catch (FileNotFoundException | NoSuchFileException | NoMetadataException e) {
			// (re)generate the cached sorted word list below
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return new ArrayList<String>();
		}

		// no cached sorted word list available
		System.out.print("Preparing the word list... ");
		Stream<String> wordListStream = fileAsString.lines().sorted();
		stringList = wordListStream.collect(Collectors.toList());
		System.out.println("OK!");
		System.out.print("Caching the prepared word list on disk... ");
		CachedWordListMetadata metadata = new CachedWordListMetadata(md5Hash);
		stringList.add(metadata.toString());
		try {
			Files.write(Paths.get(CACHEFILENAME), stringList);
			System.out.println("OK!");
		} catch (IOException e) {
			System.out.printf("%nWarning: Can't save cache file on disk, see stack trace below:%n%n");
			e.printStackTrace();
		}
		stringList.remove(stringList.size() - 1);
		
		return stringList;
	}
	
	private static String getWordListMd5Hash(String fileAsString) {
		byte[] hash = {};
		try {
			final MessageDigest SHA256 = MessageDigest.getInstance("SHA-256");
			hash = SHA256.digest(
				fileAsString.getBytes(StandardCharsets.UTF_8)
			);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return Base64.getEncoder().encodeToString(hash);
	}
}
