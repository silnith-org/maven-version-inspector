package org.silnith.util.maven;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


public class MavenVersionInspector {
    
    public static final String GROUP_ID = "groupId";
    
    public static final String ARTIFACT_ID = "artifactId";
    
    public static final String VERSION = "version";
    
    public static void main(final String[] args) throws IOException {
        final MavenVersionInspector inspector = new MavenVersionInspector();
        inspector.trialLoad();
    }
    
    public void trialLoad() throws IOException {
        final String prefix = "META-INF/maven/";
        System.out.println(prefix);
        final Enumeration<URL> resources = MavenVersionInspector.class.getClassLoader().getResources(prefix);
        while (resources.hasMoreElements()) {
            final URL url = resources.nextElement();
            System.out.println(url);
        }
    }
    
    public Collection<Properties> loadVersions() throws IOException {
        final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        final Resource[] resources = resolver.getResources("classpath*:/META-INF/maven/**/pom.properties");
        
        final ArrayList<Properties> versionProperties = new ArrayList<Properties>(resources.length);
        
        for (final Resource resource : resources) {
            final Properties properties = new Properties();
            
            properties.load(resource.getInputStream());
            
            versionProperties.add(properties);
        }
        
        return versionProperties;
    }
    
    public SortedMap<String, SortedMap<String, SortedSet<ArtifactVersion>>> sortVersions(
            final Collection<Properties> versionProperties) {
        final TreeMap<String, SortedMap<String, SortedSet<ArtifactVersion>>> groupIdMap =
                new TreeMap<String, SortedMap<String, SortedSet<ArtifactVersion>>>();
                
        for (final Properties properties : versionProperties) {
            final String groupId = properties.getProperty(GROUP_ID);
            final String artifactId = properties.getProperty(ARTIFACT_ID);
            final String version = properties.getProperty(VERSION);
            final ArtifactVersion artifactVersion = new DefaultArtifactVersion(version);
            
            final SortedMap<String, SortedSet<ArtifactVersion>> artifactIdVersions;
            if (groupIdMap.containsKey(groupId)) {
                artifactIdVersions = groupIdMap.get(groupId);
            } else {
                artifactIdVersions = new TreeMap<String, SortedSet<ArtifactVersion>>();
                groupIdMap.put(groupId, artifactIdVersions);
            }
            
            final SortedSet<ArtifactVersion> versions;
            if (artifactIdVersions.containsKey(artifactId)) {
                versions = artifactIdVersions.get(artifactId);
            } else {
                versions = new TreeSet<ArtifactVersion>();
                artifactIdVersions.put(artifactId, versions);
            }
            
            versions.add(artifactVersion);
        }
        
        return groupIdMap;
    }
    
}
