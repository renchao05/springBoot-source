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

package org.springframework.boot.actuate.autoconfigure.metrics.export.wavefront;

import java.net.URI;
import java.time.Duration;

import org.springframework.boot.actuate.autoconfigure.metrics.export.properties.PushRegistryProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

/**
 * {@link ConfigurationProperties @ConfigurationProperties} for configuring Wavefront
 * metrics export.
 *
 * @author Jon Schneider
 * @author Stephane Nicoll
 * @since 2.0.0
 */
@ConfigurationProperties("management.metrics.export.wavefront")
public class WavefrontProperties extends PushRegistryProperties {

	/**
	 * URI to ship metrics to.
	 */
	private URI uri = URI.create("https://longboard.wavefront.com");

	/**
	 * Unique identifier for the app instance that is the source of metrics being
	 * published to Wavefront. Defaults to the local host name.
	 */
	private String source;

	/**
	 * API token used when publishing metrics directly to the Wavefront API host.
	 */
	private String apiToken;

	/**
	 * Global prefix to separate metrics originating from this app's instrumentation from
	 * those originating from other Wavefront integrations when viewed in the Wavefront
	 * UI.
	 */
	private String globalPrefix;

	/**
	 * Whether to report histogram distributions aggregated into minute intervals.
	 */
	private boolean reportMinuteDistribution = true;

	/**
	 * Whether to report histogram distributions aggregated into hour intervals.
	 */
	private boolean reportHourDistribution;

	/**
	 * Whether to report histogram distributions aggregated into day intervals.
	 */
	private boolean reportDayDistribution;

	private final Sender sender = new Sender();

	public URI getUri() {
		return this.uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getApiToken() {
		return this.apiToken;
	}

	public void setApiToken(String apiToken) {
		this.apiToken = apiToken;
	}

	public String getGlobalPrefix() {
		return this.globalPrefix;
	}

	public void setGlobalPrefix(String globalPrefix) {
		this.globalPrefix = globalPrefix;
	}

	public Sender getSender() {
		return this.sender;
	}

	public boolean isReportMinuteDistribution() {
		return this.reportMinuteDistribution;
	}

	public void setReportMinuteDistribution(boolean reportMinuteDistribution) {
		this.reportMinuteDistribution = reportMinuteDistribution;
	}

	public boolean isReportHourDistribution() {
		return this.reportHourDistribution;
	}

	public void setReportHourDistribution(boolean reportHourDistribution) {
		this.reportHourDistribution = reportHourDistribution;
	}

	public boolean isReportDayDistribution() {
		return this.reportDayDistribution;
	}

	public void setReportDayDistribution(boolean reportDayDistribution) {
		this.reportDayDistribution = reportDayDistribution;
	}

	public static class Sender {

		/**
		 * Maximum queue size of the in-memory buffer.
		 */
		private int maxQueueSize = 50000;

		/**
		 * Interval at which points are flushed to the Wavefront server.
		 */
		private Duration flushInterval = Duration.ofSeconds(1);

		/**
		 * Maximum message size, such that each batch is reported as one or more messages
		 * where no message exceeds the specified size.
		 */
		private DataSize messageSize = DataSize.ofBytes(Integer.MAX_VALUE);

		public int getMaxQueueSize() {
			return this.maxQueueSize;
		}

		public void setMaxQueueSize(int maxQueueSize) {
			this.maxQueueSize = maxQueueSize;
		}

		public Duration getFlushInterval() {
			return this.flushInterval;
		}

		public void setFlushInterval(Duration flushInterval) {
			this.flushInterval = flushInterval;
		}

		public DataSize getMessageSize() {
			return this.messageSize;
		}

		public void setMessageSize(DataSize messageSize) {
			this.messageSize = messageSize;
		}

	}

}
