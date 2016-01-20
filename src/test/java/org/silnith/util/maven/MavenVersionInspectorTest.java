package org.silnith.util.maven;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.junit.Test;

public class MavenVersionInspectorTest {

	private MavenVersionInspector mavenVersionInspector = new MavenVersionInspector();

	@Test
	public void testLoadVersions() throws IOException {
		final Collection<Properties> versionProperties = mavenVersionInspector.loadVersions();
		
		assertNotNull(versionProperties);
		assertFalse(versionProperties.isEmpty());
	}

	@Test
	public void testSortVersions() {
		final Properties properties102 = new Properties();
		properties102.put("groupId", "org.silnith");
		properties102.put("artifactId", "maven-version-inspector");
		properties102.put("version", "1.0.2");
		final Properties properties103 = new Properties();
		properties103.put("groupId", "org.silnith");
		properties103.put("artifactId", "maven-version-inspector");
		properties103.put("version", "1.0.3-SNAPSHOT");
		final Properties properties098 = new Properties();
		properties098.put("groupId", "org.silnith");
		properties098.put("artifactId", "maven-version-inspector");
		properties098.put("version", "0.9.8-SNAPSHOT");
		final ArrayList<Properties> properties = new ArrayList<Properties>();
		properties.add(properties102);
		properties.add(properties103);
		properties.add(properties098);
		
		final SortedMap<String, SortedMap<String, SortedSet<ArtifactVersion>>> sortedVersions = mavenVersionInspector.sortVersions(properties);
		
		assertNotNull(sortedVersions);
		
		final TreeSet<ArtifactVersion> mavenVersionInspectorVersions = new TreeSet<ArtifactVersion>();
		mavenVersionInspectorVersions.add(new DefaultArtifactVersion("0.9.8-SNAPSHOT"));
		mavenVersionInspectorVersions.add(new DefaultArtifactVersion("1.0.2"));
		mavenVersionInspectorVersions.add(new DefaultArtifactVersion("1.0.3-SNAPSHOT"));
		
		final TreeMap<String, SortedSet<ArtifactVersion>> artifactVersionMap = new TreeMap<String, SortedSet<ArtifactVersion>>();
		artifactVersionMap.put("maven-version-inspector", mavenVersionInspectorVersions);
		
		final SortedMap<String, SortedMap<String, SortedSet<ArtifactVersion>>> expectedSortedVersions = new TreeMap<String, SortedMap<String, SortedSet<ArtifactVersion>>>();
		expectedSortedVersions.put("org.silnith", artifactVersionMap);
		
		assertEquals(expectedSortedVersions, sortedVersions);
	}

}
