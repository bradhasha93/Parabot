package TestScript;

import java.awt.Graphics;

import org.parabot.core.ui.components.LogArea;
import org.parabot.environment.api.interfaces.Paintable;
import org.parabot.environment.scripts.Category;
import org.parabot.environment.scripts.Script;
import org.parabot.environment.scripts.ScriptManifest;
import org.rev317.api.methods.SceneObjects;
import org.rev317.api.wrappers.scene.SceneObject;

@ScriptManifest(author = "Bradsta", category = Category.MAGIC, description = "Alches items", name = "SimpleAlcher", servers = { "PkHonor" }, version = 1.000)
public class TestScript extends Script implements Paintable {


	@Override
	public boolean onExecute() {
		for (SceneObject object : SceneObjects.getNearest(8987)) {
			if (object != null)
				LogArea.log("Position: " + object.getLocation().toString()
						+ " Vertex Count: "
						+ object.getModel().getVertexCount()
						+ " Triangle A length: "
						+ object.getModel().getTriangleA().length);
		}
		return true;
	}

	@Override
	public void onFinish() {
		LogArea.log("Thank you for Simple Alcher by Bradsta!");
	}


	@Override
	public void paint(Graphics g) {
	}

}
