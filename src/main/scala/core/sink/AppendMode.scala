package core.sink

/**
 * Represents the mode in which data should be written to a target.
 *
 * This trait serves as a marker for different write behaviors.
 * Implementations define how new data interacts with existing data
 * in the target (e.g., appending, overwriting, or merging).
 */
trait AppendMode

/**
 * Appends new data to the existing data without modifying or removing existing records.
 */
case class Append() extends AppendMode

/**
 * Overwrites the existing data with new data.
 *
 * Existing records in the target will be replaced entirely by the new data.
 */
case class Overwrite() extends AppendMode

/**
 * Merges new data with existing data based on specified key fields.
 *
 * This mode is useful when you want to update or insert records
 * without fully replacing the existing dataset.
 *
 * @param idFields the list of field names used to identify records for merging
 */
case class Merge(idFields: List[String]) extends AppendModeppendMode