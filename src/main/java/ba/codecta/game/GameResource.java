package ba.codecta.game;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.services.*;
import ba.codecta.game.services.model.GameResponseDto;
import ba.codecta.game.services.model.HeroDto;
import ba.codecta.game.services.InventoryService;
import ba.codecta.game.services.model.NewGameDto;
import ba.codecta.game.services.model.ShopItemsDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/game")
public class GameResource {
    @Inject
    GameService gameService;

    /**
     * POST /game
     * Creates new game, hero, level and map
     * @param newGameDto - newGameDto object mapped from JSON
     * @return GameResponseDto object
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createNewGame(NewGameDto newGameDto){
        try{
            GameResponseDto result = gameService.createNewGame(newGameDto);
            if(result == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            return Response.ok(result).build();

        }catch (Exception e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    /**
     * POST /game/{Game Id}/move?direction={up | right | down | left}
     * Handles heroes move
     * @param id - game Id
     * @param direction direction
     * @return GameResponseDto object
     */
    @POST
    @Path("/{id}/move")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleMoveAction(@PathParam("id") Integer id, @QueryParam("direction") String direction){
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
     * POST /game/{Game Id}/action?type={fight | flee | befriend | search-items}
     * @param id - game Id
     * @param type action type
     * @return GameResponseDto object
     */
    @POST
    @Path("/{id}/action")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleAction(@PathParam("id") Integer id, @QueryParam("type") String type){
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
     * PUT /game/{Game Id}/heal
     * Finds the most efficient healing item and applies it so that the player is closest to maximum health
     * @param id - game id
     * @return GameResponseDto object
     */
    @PUT
    @Path("/{id}/heal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleHealAction(@PathParam("id") Integer id){
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
     * PUT /game/{Game Id}/inventory/use/{Item Id}
     * Uses item from the inventory
     * @param id - game id
     * @param itemId - id of an item from inventory
     * @return GameResponseDto object
     */
    @PUT
    @Path("/{id}/inventory/use/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response handleHealAction(@PathParam("id") Integer id, @PathParam("itemId") Integer itemId){
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
     * GET /game/{Game Id}/shop/{Item Id}?type={weapon | item}
     * Buys item or weapon from shop
     * @return - GameResponseDto object
     */
    @POST
    @Path("{id}/shop/{itemId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getShopItems(@PathParam("id") Integer id, @PathParam("itemId") Integer itemId, @QueryParam("type") String type){
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
}