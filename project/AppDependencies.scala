import sbt.*

object AppDependencies {

  private val bootstrapVersion = "10.7.0"
  private val h2Version        = "2.5.250"
  private val playSlickVersion = "6.2.0"
  private val chimneyVersion = "1.11.0"

  val compile = Seq(
    "uk.gov.hmrc"        %% "bootstrap-backend-play-30" % bootstrapVersion,
    "com.h2database"      % "h2"                        % h2Version,
    "org.playframework"  %% "play-slick"                % playSlickVersion,
    "org.playframework"  %% "play-slick-evolutions"     % playSlickVersion,
    "io.scalaland" %% "chimney" % chimneyVersion
  )

  val test = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapVersion % Test
  )

  val it = Seq.empty
}
