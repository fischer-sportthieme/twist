import java.util.Objects;

public class CachedWordListMetadata {
	private static final byte SIZE = 2;

	private static final String STARTOFMETADATA = "METADATA"; // 0
	
	private String wordListMd5Hash; // 1
	
	public CachedWordListMetadata(String wordListMd5Hash) {
		this.setWordListMd5Hash(wordListMd5Hash);
	}

	public CachedWordListMetadata(String[] metadata) throws NoMetadataException {
		if (
			!Objects.equals(metadata[0], STARTOFMETADATA) ||
			metadata.length < SIZE
		) {
			throw new NoMetadataException(
				"Metadata of '" + WordSource.CACHEFILENAME + "' is missing"
			);
		}
		
		this.setWordListMd5Hash(metadata[1]);
	}
	
	public static CachedWordListMetadata fromCsv(String csvString) throws NoMetadataException {
		return new CachedWordListMetadata(csvString.split(","));
	}

	public String getWordListMd5Hash() {
		return this.wordListMd5Hash;
	}

	public void setWordListMd5Hash(String wordListMd5Hash) {
		this.wordListMd5Hash = wordListMd5Hash;
	}
	
	public String toString() {
		return String.join(",", STARTOFMETADATA, this.wordListMd5Hash);
	}
}
