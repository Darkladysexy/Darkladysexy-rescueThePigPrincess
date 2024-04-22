package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] pigArr;
	private Level currentLevel;

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		this.currentLevel = level;
	}

	public void update(int[][] lvlData) {
		boolean isAnyActive = false;
		for (Crabby p : currentLevel.getPigs())
			if (p.isActive()) {
				p.update(lvlData, playing);
				isAnyActive = true;
			}
		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawPigs(g, xLvlOffset);
	}


	private void drawPigs(Graphics g, int xLvlOffset) {
		for (Crabby p : currentLevel.getPigs())
			if (p.isActive()) {

				g.drawImage(pigArr[p.getState()][p.getAniIndex()], (int) p.getHitbox().x - xLvlOffset - PIG_DRAWOFFSET_X + p.flipX(),
						(int) p.getHitbox().y - PIG_DRAWOFFSET_Y + (int) p.getPushDrawOffset(), PIG_WIDTH * p.flipW(), PIG_HEIGHT, null);

//				c.drawHitbox(g, xLvlOffset);
//				c.drawAttackBox(g, xLvlOffset);
			}

	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for (Crabby p : currentLevel.getPigs())
			if (p.isActive())
				if (p.getState() != DEAD && p.getState() != HIT)
					if (attackBox.intersects(p.getHitbox())) {
						p.hurt(20);
						return;
					}	
	}

	private void loadEnemyImgs() {
		pigArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.PIG_SPRITE), 9, 5, PIG_WIDTH_DEFAULT, PIG_HEIGHT_DEFAULT);
	}

	private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
		BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
		for (int j = 0; j < tempArr.length; j++)
			for (int i = 0; i < tempArr[j].length; i++)
				tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
		return tempArr;
	}

	public void resetAllEnemies() {
		for (Crabby p : currentLevel.getPigs())
			p.resetEnemy();
	}

}
