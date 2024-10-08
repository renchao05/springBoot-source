/*
 * Copyright 2012-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.gradle.tasks.buildinfo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.ProjectLayout;
import org.gradle.api.internal.ConventionTask;
import org.gradle.api.tasks.Nested;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskExecutionException;
import org.gradle.work.DisableCachingByDefault;

import org.springframework.boot.loader.tools.BuildPropertiesWriter;
import org.springframework.boot.loader.tools.BuildPropertiesWriter.ProjectDetails;

/**
 * {@link Task} for generating a {@code build-info.properties} file from a
 * {@code Project}.
 *
 * @author Andy Wilkinson
 * @since 2.0.0
 */
@DisableCachingByDefault(because = "Not worth caching")
public class BuildInfo extends ConventionTask {

	private final BuildInfoProperties properties = new BuildInfoProperties(getProject());

	private final DirectoryProperty destinationDir;

	public BuildInfo() {
		this.destinationDir = getProject().getObjects()
			.directoryProperty()
			.convention(getProject().getLayout().getBuildDirectory());
	}

	/**
	 * Generates the {@code build-info.properties} file in the configured
	 * {@link #setDestinationDir(File) destination}.
	 */
	@TaskAction
	public void generateBuildProperties() {
		try {
			ProjectDetails details = new ProjectDetails(this.properties.getGroup(), this.properties.getArtifact(),
					this.properties.getVersion(), this.properties.getName(), this.properties.getTime(),
					coerceToStringValues(this.properties.getAdditional()));
			new BuildPropertiesWriter(new File(getDestinationDir(), "build-info.properties"))
				.writeBuildProperties(details);
		}
		catch (IOException ex) {
			throw new TaskExecutionException(this, ex);
		}
	}

	/**
	 * Returns the directory to which the {@code build-info.properties} file will be
	 * written. Defaults to the {@link ProjectLayout#getBuildDirectory() Project's build
	 * directory}.
	 * @return the destination directory
	 */
	@OutputDirectory
	public File getDestinationDir() {
		return this.destinationDir.getAsFile().get();
	}

	/**
	 * Sets the directory to which the {@code build-info.properties} file will be written.
	 * @param destinationDir the destination directory
	 */
	public void setDestinationDir(File destinationDir) {
		this.destinationDir.set(destinationDir);
	}

	/**
	 * Returns the {@link BuildInfoProperties properties} that will be included in the
	 * {@code build-info.properties} file.
	 * @return the properties
	 */
	@Nested
	public BuildInfoProperties getProperties() {
		return this.properties;
	}

	/**
	 * Executes the given {@code action} on the {@link #getProperties()} properties.
	 * @param action the action
	 */
	public void properties(Action<BuildInfoProperties> action) {
		action.execute(this.properties);
	}

	private Map<String, String> coerceToStringValues(Map<String, Object> input) {
		Map<String, String> output = new HashMap<>();
		input.forEach((key, value) -> output.put(key, (value != null) ? value.toString() : null));
		return output;
	}

}
