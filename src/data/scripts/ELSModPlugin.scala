package data.scripts

import com.fs.starfarer.api.{BaseModPlugin, Global}
import org.dark.shaders.util.ShaderLib

/**
  * Created by Eliza on 6/28/16.
  */
class ELSModPlugin
extends BaseModPlugin {
  lazy val hasGraphicsLib = Global.getSettings.getModManager
                                  .isModEnabled("shaderLib")

  override def onApplicationLoad(): Unit = {
    ShaderLib.init()
  }

}
