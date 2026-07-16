import sbt.*

object AppDependencies {

  private val bootstrapVersion = "10.7.0"

  val compile = Seq(
    "uk.gov.hmrc"        %% "bootstrap-backend-play-30" % bootstrapVersion,
    "com.github.blemale" %% "scaffeine"                 % "5.3.0"
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapVersion % Test
  )

  val it = Seq.empty
}
