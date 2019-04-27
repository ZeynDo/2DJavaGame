package com.zeynep.game.states;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.sun.org.apache.xpath.internal.operations.Or;
import com.zeynep.game.entities.HUD;
import com.zeynep.game.entities.Player;
import com.zeynep.game.handlers.B2DVars;
import com.zeynep.game.handlers.GameStateManager;
import com.zeynep.game.handlers.MyContactListener;
import com.zeynep.game.handlers.MyInput;
import com.zeynep.game.main.Game;

import static com.zeynep.game.handlers.B2DVars.*;

public class Play extends GameState{
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private final float GRAVITY = -9.81f;
    private OrthographicCamera b2dCamera;
    private final String GROUND_ID = "ground";
    private MyContactListener contactListener;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private float tileSize;
    private Player player;
    final float SPEED = 0.2f;
    private HUD hud;

    public Play(GameStateManager gameStateManager) {
        super(gameStateManager);

        contactListener = new MyContactListener();

        //setup box2d stuff
        world = new World(new Vector2(0,GRAVITY),true);
        world.setContactListener(contactListener);
        box2DDebugRenderer = new Box2DDebugRenderer();
        //create platform




        //create Player
        createPlayer();

        //create Tiles
        createTiles();



        //set up orthographic camera
        b2dCamera = new OrthographicCamera();
        b2dCamera.setToOrtho(false, Game.V_WIDTH/PIXEL_PER_METER, Game.V_HEIGHT/PIXEL_PER_METER);

        //set up hud
        hud = new HUD(player);

        /////////////////////////////////////////// tile map


    }

    @Override
    public void handleInput() {
        //player jump
        if(MyInput.isPressed(MyInput.BUTTON1)){
            if (contactListener.isPlayerOnGround()) {
                player.getBody().applyForceToCenter(0,300, true);
            }
        }
        if(MyInput.isPressed(MyInput.BUTTON2)){
            switchBlocks();
        }
    }
    @Override
    public void dispose() {}
    @Override
    public void render() {
        //clear screen
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);





        //draw tile map
        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();

        //draw player
        spriteBatch.setProjectionMatrix(camera.combined);
        player.render(spriteBatch);

        box2DDebugRenderer.render(world,b2dCamera.combined);

        //draw hud
        spriteBatch.setProjectionMatrix(hudCamera.combined);
        hud.render(spriteBatch);


    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt,6,2);
        player.update(dt);
    }

    public void createPlayer() {
        BodyDef bodyDef = new BodyDef();
        PolygonShape polygonShape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();

        bodyDef.position.set(160/ PIXEL_PER_METER,200/PIXEL_PER_METER);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearVelocity.set(SPEED,0);
        Body body = world.createBody(bodyDef);

        polygonShape.setAsBox(13/PIXEL_PER_METER,13/PIXEL_PER_METER);
        fixtureDef.shape = polygonShape;
        // bouncing: fixtureDef.restitution =0.2f;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_RED;
        body.createFixture(fixtureDef).setUserData("player");

        //create foot sensor
        polygonShape.setAsBox(13/PIXEL_PER_METER,2/PIXEL_PER_METER, new Vector2(0,-13/PIXEL_PER_METER),0);
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_RED;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");



        //create player
        player = new Player(body);

        body.setUserData(player);
    }
    public void createTiles() {
        //load tile map
        tiledMap = new TmxMapLoader().load("lvl1.3.tmx");
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        tileSize = (Integer) tiledMap.getProperties().get("tilewidth");


        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("red");
        createLayer(layer, B2DVars.BIT_RED);

         layer = (TiledMapTileLayer) tiledMap.getLayers().get("blue");
        createLayer(layer, B2DVars.BIT_BLUE);

         layer = (TiledMapTileLayer) tiledMap.getLayers().get("green");
        createLayer(layer, B2DVars.BIT_GREEN);



    }
    private void createLayer(TiledMapTileLayer layer, short bits) {
        BodyDef bodyDef = new BodyDef();

        FixtureDef fixtureDef = new FixtureDef();


        //go through all the cells in the layer
        for(int row =0; row < layer.getHeight(); row++){
            for(int col =0; col < layer.getWidth(); col++){

                //get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col,row);
                //check if cell exists!!
                if(cell ==null) continue;
                if(cell.getTile() ==null) continue;

                //create a body + fixure from cell
                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(
                        (col + 0.5f)*tileSize/PIXEL_PER_METER,
                        (row + 0.5f)*tileSize/PIXEL_PER_METER
                );
                ChainShape chainShape = new ChainShape();
                Vector2[] vector2 = new Vector2[3];
                vector2[0]= new Vector2(
                        -tileSize/2/PIXEL_PER_METER, -tileSize/2/PIXEL_PER_METER);

                vector2[1] = new Vector2(
                        -tileSize/2/PIXEL_PER_METER, tileSize/2/PIXEL_PER_METER);

                vector2[2] = new Vector2(
                        tileSize/2/PIXEL_PER_METER, tileSize/2/PIXEL_PER_METER);
                chainShape.createChain(vector2);
                fixtureDef.friction = 0;
                fixtureDef.shape = chainShape;
                fixtureDef.filter.categoryBits = bits;
                fixtureDef.filter.maskBits = B2DVars.BIT_PLAYER;
                fixtureDef.isSensor = false;

                world.createBody(bodyDef).createFixture(fixtureDef);

            }
        }
    }
    private void switchBlocks() {
        Filter filter = player.getBody().getFixtureList().first().getFilterData();
        short bits = filter.maskBits;

        //switch to next color
        //red green blue red
        if((bits & BIT_RED )!=0 ) {
            bits &= ~BIT_RED;
            bits |= BIT_GREEN;
        } else if((bits & BIT_GREEN )!=0 ) {
            bits &= ~BIT_GREEN;
            bits |= BIT_BLUE;
        } else if((bits & BIT_BLUE )!=0 ) {
            bits &= ~BIT_BLUE;
            bits |= BIT_RED;
        }

        //set new mask bits
        filter.maskBits = bits;
        player.getBody().getFixtureList().first().setFilterData(filter);

        //set new mask bits for foot
        filter = player.getBody().getFixtureList().get(1).getFilterData();
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(1).setFilterData(filter);
    }
}
