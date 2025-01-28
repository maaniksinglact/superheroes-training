package com.cleartax.training_superheroes.services;

import com.cleartax.training_superheroes.dto.Superhero;
import com.cleartax.training_superheroes.dto.SuperheroRequestBody;
import com.cleartax.training_superheroes.repos.SuperheroRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SuperheroService {

    private SuperheroRepository superheroRepository;

    public SuperheroService(SuperheroRepository superheroRepository){
        this.superheroRepository = superheroRepository;
    }

    public Superhero getSuperhero(String name, String universe){
        if(null != name){
            return getByName(name);
//        } else if (null != universe){
//            return getByUniverse(universe);
        } else{
            throw new RuntimeException("Not found");
        }
    }

    private Superhero getByName(String name){
        return getDummyDate(name);
    }

//    private Superhero  getByUniverse(String universe){
//        Superhero superhero =  new Superhero();
//        superhero.setUniverse(universe);
//        return superhero;
//    }

    private Superhero getDummyDate(String name){
        //Superhero superhero =  new Superhero();
        //superhero.setName(name);
        return superheroRepository.findByName(name).get();
    }

    public Superhero persistSuperhero(SuperheroRequestBody requestBody){
        Superhero superhero = new Superhero();
        superhero.setName(requestBody.getName());
        superhero.setPower(requestBody.getPower());
        superhero.setUniverse(requestBody.getUniverse());

        return superheroRepository.save(superhero);
    }
    public Superhero updateSuperhero(String superheroname,SuperheroRequestBody updatesuperhero){
        //String superheroname2 = superheroname;
        Optional <Superhero> findsuperhero=superheroRepository.findByName(superheroname);
        Superhero temp0=new Superhero();
        if(findsuperhero.isPresent()){
            temp0=findsuperhero.get();
             if(updatesuperhero.getName()!=null){
                  temp0.setName(updatesuperhero.getName());
             }
             if(updatesuperhero.getUniverse()!=null){
                 temp0.setUniverse(updatesuperhero.getUniverse());
             }
             if(updatesuperhero.getPower()!=null){
                 temp0.setPower(updatesuperhero.getPower());
             }
             superheroRepository.save(temp0);
             return temp0;
        }
        throw new RuntimeException("not found");
    }
    public Superhero deleteSuperhero(String name){
         Optional <Superhero> findsuperhero=superheroRepository.findByName(name);
         if(findsuperhero.isPresent()){
             superheroRepository.deleteByName(name);
             return findsuperhero.get();
         }
         throw new RuntimeException("not found");
    }
}
