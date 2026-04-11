package core.pipeline

/**
 * Represents metadata describing a data pipeline.
 *
 * This class contains identifying and descriptive information
 * about a pipeline, which can be used for documentation,
 * logging, monitoring, or UI display purposes.
 *
 * Example usage:
 * {{{
 *   val metadata = DataPipelineMetadata(
 *     displayName = "User Ingestion Pipeline",
 *     description = "Ingests and processes user data from source systems",
 *     id = "user-ingestion-pipeline"
 *   )
 * }}}
 *
 * @param displayName a human-readable name for the pipeline
 * @param description a brief description of what the pipeline does
 * @param id a unique identifier for the pipeline
 */
case class DataPipelineMetadata
(
  displayName: String,
  description: String,
  id: String
)

