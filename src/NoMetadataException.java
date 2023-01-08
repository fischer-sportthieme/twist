import java.io.IOException;

public class NoMetadataException extends IOException {
	@java.io.Serial
	private static final long serialVersionUID = 711482447494671510L;

	public NoMetadataException() {
        super();
    }
	
	public NoMetadataException(String message) {
        super(message);
    }
	
	public NoMetadataException(String message, Throwable cause) {
        super(message, cause);
    }
	
	public NoMetadataException(Throwable cause) {
        super(cause);
    }
}
