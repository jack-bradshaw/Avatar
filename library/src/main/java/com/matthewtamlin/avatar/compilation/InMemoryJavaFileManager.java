package com.matthewtamlin.avatar.compilation;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.matthewtamlin.java_utilities.checkers.NullChecker.checkNotNull;

/**
 * A Java file manager which stores files in memory.
 */
public class InMemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {
	private static final String BASE_LOCATION = "memory:///";
	
	private final Map<URI, JavaFileObject> files = new HashMap<>();
	
	/**
	 * Constructs a new InMemoryJavaFileManager by wrapping the supplied file manager and storing its files in memory.
	 *
	 * @param fileManager
	 * 		the file manager to wrap, not null
	 */
	public InMemoryJavaFileManager(final JavaFileManager fileManager) {
		super(fileManager);
	}
	
	@Override
	public boolean isSameFile(FileObject a, FileObject b) {
		return a.toUri().equals(b.toUri());
	}
	
	@Override
	public FileObject getFileForInput(
			final Location location,
			final String packageName,
			final String relativeName)
			throws IOException {
		
		if (location.isOutputLocation()) {
			final URI uri = createUri(location, packageName, relativeName);
			return files.containsKey(uri) ? files.get(uri) : null;
			
		} else {
			return super.getFileForInput(location, packageName, relativeName);
		}
	}
	
	@Override
	public JavaFileObject getJavaFileForInput(
			final Location location,
			final String className,
			final Kind kind)
			throws IOException {
		
		if (location.isOutputLocation()) {
			final URI uri = createUri(location, className, kind);
			
			if (!files.containsKey(uri)) {
				files.put(uri, new InMemoryJavaFileObject(uri));
			}
			
			return files.get(uri);
			
		} else {
			return super.getJavaFileForInput(location, className, kind);
		}
	}
	
	@Override
	public FileObject getFileForOutput(
			final Location location,
			final String packageName,
			final String relativeName,
			final FileObject sibling)
			throws IOException {
		
		final URI uri = createUri(location, packageName, relativeName);
		
		if (!files.containsKey(uri)) {
			files.put(uri, new InMemoryJavaFileObject(uri));
		}
		
		return files.get(uri);
	}
	
	@Override
	public JavaFileObject getJavaFileForOutput(
			final Location location,
			final String className,
			final Kind kind,
			final FileObject sibling)
			throws IOException {
		
		final URI uri = createUri(location, className, kind);
		
		if (!files.containsKey(uri)) {
			files.put(uri, new InMemoryJavaFileObject(uri));
		}
		
		return files.get(uri);
	}
	
	@Override
	public void close() throws IOException {
		super.close();
	}
	
	/**
	 * @return all output files, may be empty, not null
	 */
	public Set<JavaFileObject> getOutputFiles() {
		return new HashSet<>(files.values());
	}
	
	/**
	 * Creates a URI for a class file of the form "BASE_LOCATION" + "location" + "/" + "packageName" + "/" +
	 * "relativeName". All '.' characters in the packageName are replaced with '/' characters.
	 *
	 * @return the URI
	 */
	private static URI createUri(
			final JavaFileManager.Location location,
			final String packageName,
			final String relativeName) {
		
		final StringBuilder uri = new StringBuilder();
		
		uri.append(BASE_LOCATION);
		uri.append(location.getName());
		uri.append("/");
		
		if (!packageName.isEmpty()) {
			uri.append(packageName.replace('.', '/'));
			uri.append("/");
		}
		
		uri.append(relativeName);
		
		return URI.create(uri.toString());
	}
	
	/**
	 * Creates a URI for a class file. The URI is of the form "BASE_LOCATION" + "location" + "/" + "className" +
	 * "extension", where all '.' characters in the className are replaced with '/' characters, and the extension is
	 * defined by the kind.
	 */
	private static URI createUri(
			final JavaFileManager.Location location,
			final String className,
			final JavaFileObject.Kind kind) {
		
		checkNotNull(location, "Argument \'location\' cannot be null.");
		checkNotNull(className, "Argument \'className\' cannot be null.");
		checkNotNull(kind, "Argument \'kind\' cannot be null.");
		
		final StringBuilder uri = new StringBuilder();
		
		uri.append(BASE_LOCATION);
		uri.append(location.getName());
		uri.append("/");
		uri.append(className.replace(".", "/"));
		uri.append(kind.extension);
		
		return URI.create(uri.toString());
	}
}