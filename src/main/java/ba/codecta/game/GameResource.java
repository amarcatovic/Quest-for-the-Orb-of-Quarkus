package ba.codecta.game;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.services.*;
import ba.codecta.game.services.model.*;
import ba.codecta.game.services.InventoryService;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.annotation.security.PermitAll;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

@Path("/game")
@RequestScoped
public class GameResource {
    @Inject
    GameService gameService;

    @Inject
    JsonWebToken jwt;

    /**
     * POST /game
     * Creates new game, hero, level and map
     * @param newGameDto - newGameDto object mapped from JSON
     * @return GameCreateResponseDto object
     */
    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createNewGame(NewGameDto newGameDto){
        try{
            GameCreateResponseDto result = gameService.createNewGame(newGameDto);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * POST /game/new-level
     * Creates new game level if player has killed the boss monster
     * @param ctx - SecurityContext Object
     * @return GameCreateResponseDto object
     */
    @POST
    @Path("/new-level")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewLevel(@Context SecurityContext ctx){
        Integer heroId = this.getHeroIdFromToken(ctx);
        if(heroId == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try{
            GameCreateResponseDto result = gameService.createNewLevel(heroId);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * POST /game/move?direction={up | right | down | left}
     * Handles heroes move
     * @param direction direction
     * @param ctx - SecurityContext Object
     * @return GameResponseDto object
     */
    @POST
    @Path("/move")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleMoveAction(@QueryParam("direction") String direction, @Context SecurityContext ctx){
        Integer id = this.getGameIdFromToken(ctx);
        if(id == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try{
            GameResponseDto result = gameService.handleMoveAction(id, direction);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * POST /game/action?type={fight | flee | befriend | search-items}
     * @param ctx - SecurityContext Object
     * @param type action type
     * @return GameResponseDto object
     */
    @POST
    @Path("/action")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleAction(@Context SecurityContext ctx, @QueryParam("type") String type){
        Integer id = this.getGameIdFromToken(ctx);
        if(id == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try{
            GameResponseDto result = gameService.handleAction(id, type);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * PUT /game/heal
     * Finds the most efficient healing item and applies it so that the player is closest to maximum health
     * @param ctx - SecurityContext Object
     * @return GameResponseDto object
     */
    @PUT
    @Path("/heal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleHealAction(@Context SecurityContext ctx){
        Integer id = this.getGameIdFromToken(ctx);
        if(id == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try{
            GameResponseDto result = gameService.handleHealAction(id);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * PUT /game/inventory/use/{Item Id}
     * Uses item from the inventory
     * @param ctx - SecurityContext Object
     * @param itemId - id of an item from inventory
     * @return GameResponseDto object
     */
    @PUT
    @Path("/inventory/use/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleHealAction(@Context SecurityContext ctx, @PathParam("itemId") Integer itemId){
        Integer id = this.getGameIdFromToken(ctx);
        if(id == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try{
            GameResponseDto result = gameService.handleInventoryAction(id, itemId);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * GET /game/shop
     * Gets all shop items
     * @return - ShopItemsDto object
     */
    @GET
    @Path("/shop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShopItems(){
        try{
            ShopItemsDto result = gameService.getShopItems();
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * GET /game/shop/{Item Id}?type={weapon | item}
     * Buys item or weapon from shop
     * @param ctx - SecurityContext Object
     * @return - GameResponseDto object
     */
    @POST
    @Path("/shop/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShopItems(@Context SecurityContext ctx, @PathParam("itemId") Integer itemId, @QueryParam("type") String type){
        Integer id = this.getGameIdFromToken(ctx);
        if(id == null){
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try{
            GameResponseDto result = gameService.handleShopAction(id, itemId, type);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * Extracts Game Id from token
     * @param ctx - SecurityContext object
     * @return null if there is no token, Game Id otherwise
     */
    private Integer getGameIdFromToken(SecurityContext ctx){
        if(jwt.getClaimNames() == null){
            return null;
        }

        return Integer.parseInt(jwt.getClaim("gameId").toString());
    }

    /**
     * Extracts Hero Id from token
     * @param ctx - SecurityContext object
     * @return null if there is no token, Hero Id otherwise
     */
    private Integer getHeroIdFromToken(SecurityContext ctx){
        if(jwt.getClaimNames() == null){
            return null;
        }

        return Integer.parseInt(jwt.getClaim("heroId").toString());
    }
}