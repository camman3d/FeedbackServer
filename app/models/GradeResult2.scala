package models

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 9/30/13
 * Time: 11:00 AM
 * To change this template use File | Settings | File Templates.
 */
case class GradeResult2(feature: String, results: List[TestCaseResult], score: Double, possible: Double, extraCredit: Boolean, notes: String)
