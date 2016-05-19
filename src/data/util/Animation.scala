package data.util

import Fractional.Implicits._
/**
  * Created by hawk on 5/4/16.
  */
object Animation {

  def map[N: Fractional](in_min: N, in_max: N, out_min: N, out_max: N)(x: N): N
    = (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min

}
