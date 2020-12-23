package ba.codecta.game;

import ba.codecta.game.helper.MapAction;
import ba.codecta.game.helper.MoveDirection;
import ba.codecta.game.repository.entity.HeroEntity;
import ba.codecta.game.services.*;
import ba.codecta.game.services.model.HeroDto;
import ba.codecta.game.services.InventoryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/game")
public class GameResource {
    @Inject
    HeroService heroService;

    @Inject
    MapService mapService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello() {
        HeroDto result = heroService.createHero("Amar", "Test");
        return Response.ok(result).build();
    }

    @GET
    @Path("/2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello2() {
        return Response.ok(mapService.createMap(1)).build();
    }

    @GET
    @Path("/3")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello3() {
        return Response.ok(mapService.move(1, MoveDirection.DOWN)).build();
    }

    @GET
    @Path("/4")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello4() {
        return Response.ok(mapService.action(1, 1 ,MapAction.FIGHT)).build();
    }

    @GET
    @Path("/5")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello5() {
        return Response.ok(mapService.action(1, 1 ,MapAction.SEARCH_SECRET_ITEM)).build();
    }

    @GET
    @Path("/6")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hello6() {
        return Response.ok(mapService.action(1, 1 ,MapAction.BEFRIEND)).build();
    }
}