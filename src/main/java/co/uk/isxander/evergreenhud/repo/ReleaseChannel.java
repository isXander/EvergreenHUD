package co.uk.isxander.evergreenhud.repo;

public enum ReleaseChannel {
    RELEASE("release"),
    BETA("prerelease");

    public final String jsonName;
    ReleaseChannel(String jsonName) {
        this.jsonName = jsonName;
    }
}
