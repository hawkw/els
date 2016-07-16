package data.scripts.world

import com.fs.starfarer.api.campaign.{SectorAPI, SectorGeneratorPlugin}
import com.fs.starfarer.api.impl.campaign.shared.SharedData
import data.scripts.world.systems.Rousseau

/**
  * Created by hawk on 7/12/16.
  */
class ELSGen
extends SectorGeneratorPlugin {
  override def generate(sector: SectorAPI): Unit = {

    SharedData.getData.getPersonBountyEventData
              .addParticipatingFaction("directory")

    Rousseau generate sector
  }
}
