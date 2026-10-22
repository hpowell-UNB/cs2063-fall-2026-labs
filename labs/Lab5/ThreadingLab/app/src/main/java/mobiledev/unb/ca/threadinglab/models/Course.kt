package mobiledev.unb.ca.threadinglab.models

/**
 * This makes use of the data class pattern.  All values are public meaning 
 * they can be accessed through getters (for example -> course.name).
 *  HINT: Set each objects value using the constructor
 */
data class Course(val id: String,
                  val name: String,
                  val description: String)
