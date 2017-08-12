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
	public boolean isSameFile(final FileObject a, final FileObject b) {
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
	 * Creates a URI for a class file using a location, a package name, and a relative class name.
	 *
	 * @param location
	 * 		the location of the file relative to {@code BASE_LOCATION}, not null
	 * @param packageName
	 * 		the package name of the class, not null
	 * @param relativeName
	 * 		the name of the class, relative to the package name, not null
	 *
	 * @return the URI, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code location} is null
	 * @throws IllegalArgumentException
	 * 		if {@code packageName} is null
	 * @throws IllegalArgumentException
	 * 		if {@code relativeName} is null
	 */
	private static URI createUri(
			final JavaFileManager.Location location,
			final String packageName,
			final String relativeName) {
		
		checkNotNull(location, "Argument \'location\' cannot be null.");
		checkNotNull(packageName, "Argument \'packageName\' cannot be null.");
		checkNotNull(relativeName, "Argument \'relativeName\' cannot be null.");
		
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
	 * Creates a URI for a class file using a location, a class name, and a kind.
	 *
	 * @param location
	 * 		the location of the file relative to {@code BASE_LOCATION}, not null
	 * @param className
	 * 		the name of the class, not null
	 * @param kind
	 * 		the kind of the class, not null
	 *
	 * @return the URI, not null
	 *
	 * @throws IllegalArgumentException
	 * 		if {@code location} is null
	 * @throws IllegalArgumentException
	 * 		if {@code className} is null
	 * @throws IllegalArgumentException
	 * 		if {@code kind} is null
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