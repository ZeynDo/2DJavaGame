package com.zeynep.game.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zeynep.game.handlers.Content;
import com.zeynep.game.handlers.GameStateManager;
import com.zeynep.game.handlers.MyInput;
import com.zeynep.game.handlers.MyInputProcessor;
import com.badlogic.gdx.graphics.Texture;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	private SpriteBatch spriteBatch;
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;
	private GameStateManager gameStateManager;

	public static final String TITLE="Alien Jump";
	public static final int V_WIDTH= 320;
	public static final int V_HEIGHT= 240;
	public static final int SCALE= 2;

	public static Content resources;

	public static final float STEP= 1/60f;
	private float accumulate;


	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public OrthographicCamera getHudCamera() {
		return hudCamera;
	}



	@Override
	public void create () {


		Gdx.input.setInputProcessor(new MyInputProcessor() {
		}) ;

		resources = new Content();
		resources.loadTexture("images/player.png","alien");
		resources.loadTexture("images/hud.png","hud");

		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false,V_WIDTH,V_HEIGHT);
		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false,V_WIDTH,V_HEIGHT);
		gameStateManager = new GameStateManager(this);
	}

	@Override
	public void render () {
		accumulate += Gdx.graphics.getDeltaTime();
		while(accumulate >= STEP){
			accumulate -= STEP;
			gameStateManager.update(STEP);
			gameStateManager.render();
			MyInput.update();
		}

	}
	
	@Override
	public void dispose () {

	}
}
