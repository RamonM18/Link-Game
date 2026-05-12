# CSCE 31903 Programming Paradigms
# Fall 20251130
# Assignment 7 starter code
# Ramon Morales
# this is the python version of our link game. It will have everything that 4 and 6 did plus the additon of Cuccos. 

import pygame
import time
import json
import math

from pygame.locals import*
from time import sleep


class Sprite():
    def __init__(self, x, y, w, h, image):
        self.x = x
        self.y = y
        self.w = w
        self.h = h
        self.speed = 5
        self.valid = True
        self.image = pygame.image.load(image)

        #position variables
        self.px = x
        self.py = y

    def update(self):
        return self.valid

    def is_link(self):
        return False
    
    def is_tree(self):
        return False
    
    def is_treasure_chest(self):
        return False
    
    def is_boomerang(self):
        return False
    
    def is_cucco(self):
        return False

    # for the starter code, we assume that all Sprites of a certain
    # type are the same size, and thus don't need w and h saved
    # However, it would be very easy to add more attributes to be 
    # saved here!
    def marshal(self):
        return {
            "x": self.x,
            "y": self.y
        }
    
    #method for sprites to draw themselves
    def draw_yourself(self, screen, scrollX, scrollY):
        LOCATION = (self.x - scrollX, self.y - scrollY)
        SIZE = (self.w, self.h)
        screen.blit(pygame.transform.scale(self.image, SIZE), LOCATION)

    #detection method to see if we're clicking where a sprite is already at
    def detecting_sprites(self, existing_x, existing_y):
        if existing_x >= self.x and existing_x < self.x + self. w and existing_y >= self.y and existing_y < self.y + self.h:
            return True
        else:
            return False


class Tree(Sprite):
    # variables that belong to the class, not to a specific
    # instance of the class - this is similar to Java's static variables
    TREE_WIDTH = 75
    TREE_HEIGHT = 75
    num_tree = 0
    
    # this method belongs to the class itself and does not need
    # the 'self' object
    @staticmethod
    def reset_tree():
        Tree.num_tree = 0
    
    # constructor with default values - one of the ways you can
    # mimic Java's overloaded constructors
    # in this example, if w and h are provided, create the tree
    # as defined. If they are not provided, create a tree at 
    # half the regular size
    def __init__(self, x, y, w=None, h=None):
        if w is None or h is None:
            super().__init__(x, y, Tree.TREE_WIDTH, Tree.TREE_HEIGHT, "images/tree.png")
        else:
            super().__init__(x, y, w, h, "images/tree.png")
        Tree.num_tree += 1

    def is_tree(self):
        return True
    
    def to_String(self):
        return "Tree (x,y) = (" + str(self.x) + ", " + str(self.y) + "), w = " + str(self.w) + ", h = " + str(self.h)

class Link(Sprite):
    LINK_WIDTH = 50
    LINK_HEIGHT = 60
    
    
    def __init__(self, x, y):
        super().__init__(x, y, Link.LINK_WIDTH, Link.LINK_HEIGHT, "images/link1.png")
        #array for the images
        self.images = []

        #variables for direction, total images, and max images in each direction
        self.MAX_IMAGES_PER_DIRECTION = 11
        self.TOTAL_NUM_IMAGES = 44
        self.NUM_DIRECTIONS = 4
        self.image_frame_num = 0
        self.direction = 1
        #gems gathered variable
        self.num_gems_gathered = 0

        index = 1
        #for loop for the array to read in the link images
        for i in range(self.NUM_DIRECTIONS):
            self.images.append([])
            for j in range(self.MAX_IMAGES_PER_DIRECTION):
                self.images[i].append(pygame.image.load(f"images/link{index}.png"))
                index += 1

    # method for Link to draw himself
    def draw_yourself(self, screen, scrollX, scrollY):
        LOCATION = (self.x - scrollX, self.y - scrollY)
        SIZE = (self.w, self.h)
        screen.blit(pygame.transform.scale(self.images[self.direction][self.image_frame_num], SIZE), LOCATION)

    # update method for link, returns default is valid
    def update(self):
        return self.valid

    # boolean method that shows link is link by returning true instead of false like it is in the sprite class
    def is_link(self):
        return True

    # method for changes link's facing direction based on if he's going left, right, up or down
    # it takes the x and y and either adds or minuses the speed and then changes the direction from 0 to 3
    def move_self(self, direction):

        self.image_frame_num +=1
        if self.image_frame_num >= self.MAX_IMAGES_PER_DIRECTION:
            self.image_frame_num = 0

        if direction == "up":
            self.y -= self.speed
            self.direction = 3

        if direction == "down":
            self.y += self.speed
            self.direction = 0

        if direction == "left":
            self.x -= self.speed
            self.direction = 1

        if direction == "right":
            self.x += self.speed
            self.direction = 2

    #method to have the previous x and y
    def save_previous_position(self):
        self.px = self.x 
        self.py = self.y 

    # String method that returns link's x,y,w,h
    def to_string(self):
        return "Link (x,y) = (" + str(self.x) + ", " + str(self.y) + "), w = " + str(self.w) + ", h = " + str(self.h)
    
    # collision fixing for link
    def fix_collision(self, t):
        if self.px + self.w <= t.x and self.x + self.w >= t.x:
            self.x = t.x - self.w
        elif self.px >= t.x + t.w and self.x < t.x + t.w:
            self.x = t.x + t.w
        if self.py + self.h <= t.y and self.y + self.h >= t.y:
            self.y = t.y - self.h
        elif self.py >= t.y + t.h and self.y <= t.y + t.h:
            self.y = t.y + t.h

    # method that increments number of gems gathered and is called in the model when link collides with a rupee
    def gems_gathered(self):
        self.num_gems_gathered +=1


class TreasureChest(Sprite):
    CHEST_WIDTH = 30
    CHEST_HEIGHT = 40

    def __init__(self,x,y):
        super().__init__(x, y, TreasureChest.CHEST_WIDTH, TreasureChest.CHEST_HEIGHT, "images/treasurechest.png")
        self.chest_opened = False
        self.frames_since_opened = 0

        #loading in the chest and rupee image
        self.images = []
        self.images.append(pygame.image.load("images/treasurechest.png"))
        self.images.append(pygame.image.load("images/rupee.png"))

    #for when link or the boomerang collide with the chest. It will be called in model to turn the chest into an opened chest aka the rupee
    def treasure_chest_opened(self):
        self.chest_opened = True
        self.image = self.images[1]
        self.frames_since_opened = 0

    #update method that handles the frame count for when the chest is opened and when it exceeds 40, it turns valid to false and the rupee is removed
    def update(self):
        if self.chest_opened == True:
            self.frames_since_opened +=1
        if self.frames_since_opened >= 40:
            self.valid = False
        return self.valid

    #boolean that returns true to show that the chest is the chest
    def is_treasure_chest(self):
        return True
    
    #string method that returns chest's x,y,w,h
    def to_string(self):
        return "treasureChest (x,y) = (" + str(self.x) + ", " + str(self.y) + "), w = " + str(self.w) + ", h = " + str(self.h)

class Boomerang(Sprite):
    BOOMERANG_WIDTH = 15
    BOOMERANG_HEIGHT = 15

    def __init__(self, x, y, image, direction):
        super().__init__(x, y, Boomerang.BOOMERANG_WIDTH, Boomerang.BOOMERANG_HEIGHT, image)
        #variables for direction, total images, index and image frame
        self.images = []
        self.MAX_IMAGES = 4
        self.image_frame = 0
        self.direction = direction 
        
        index = 1
        for i in range(self.MAX_IMAGES):
            self.images.append(pygame.image.load(f"images/boomerang{index}.png"))
            index += 1
    
    #update method that handles direction. If link is facing left, the boomerang will come out or be thrown left. Same for right, up and down
    def update(self):
        #if direciton is down
        if self.direction == 0:
            self.y += self.speed
        #if direction is left
        elif self.direction == 1:
            self.x -= self.speed
        #if direction is right
        if self.direction == 2:
            self.x += self.speed
        #if direction is up
        elif self.direction == 3:
            self.y -= self.speed
        
        #increasing the image frame 
        self.image_frame += 1

        #using %4 to rotate back through images. it goes from 0 to 3 for the boomerang images
        self.image_frame = self.image_frame %4

        #updates which boomerang to show
        self.image = self.images[self.image_frame]

        return self.valid
    
    #boolean that returns true for boomerang
    def is_boomerang(self):
        return True
    
    #method for when boomerang collides and it changes the valid to false so that it is removed or "destroyed"
    def boomerang_destroyed(self):
        self.valid = False

    # String method that returns boomerang's x,y,w,h
    def to_string(self):
        return "Boomerang (x,y) = (" + str(self.x) + ", " + str(self.y) + "), w = " + str(self.w) + ", h = " + str(self.h)

class Cucco(Sprite):
    CUCCO_WIDTH = 30
    CUCCO_HEIGHT = 30

    #variables
    num_cucco = 0
    cuccos_angry = False
    num_hits = 0
    num_cuccos_disappeared = 0
    linkx = 0
    linky = 0

    def __init__(self, x, y):
        super().__init__(x, y, Cucco.CUCCO_WIDTH, Cucco.CUCCO_HEIGHT, "images/cucco1.png")
        #creating the two list for regular cuccos and angry cuccos
        self.images = []
        self.angry_images = []
        #variables need for cucco direction, timer, boolean, x and y direction, and frame number
        self.image_frame = 0
        self.x_direction = 1
        self.y_direction = 1
        self.attached_to_link = False
        self.attached_to_link_timer = 0
        self.direction = 1
        self.NUM_DIRECTIONS = 2
        self.MAX_IMAGES_PER_DIRECTION = 2
        self.angry_speed = 8
        self.speed = 2

        #incrementing num_cucco
        Cucco.num_cucco += 1

        index = 1
        #for loop to read in the regular cucco images
        for i in range(self.NUM_DIRECTIONS):
            self.images.append([])
            for j in range(self.MAX_IMAGES_PER_DIRECTION):
                self.images[i].append(pygame.image.load(f"images/cucco{index}.png"))
                index += 1

        #have to reset index
        index = 1
        #for loop to read in the angry cucco images
        for i in range(self.NUM_DIRECTIONS):
            self.angry_images.append([])
            for j in range(self.MAX_IMAGES_PER_DIRECTION):
                self.angry_images[i].append(pygame.image.load(f"images/angrycucco{index}.png"))
                index += 1

    # method that checks if the cucco is normal or angry. if its not angry, it will draw the regular ones. Else if it is angry, it draws the angry ones
    def draw_yourself(self, screen, scrollX, scrollY):
        LOCATION = (self.x - scrollX, self.y - scrollY)
        SIZE = (self.w, self.h)
        if Cucco.cuccos_angry == False:
            screen.blit(pygame.transform.scale(self.images[self.direction][self.image_frame], SIZE), LOCATION)
        elif Cucco.cuccos_angry == True:
            screen.blit(pygame.transform.scale(self.angry_images[self.direction][self.image_frame], SIZE), LOCATION)

    #update method
    def update(self):
        #checking to see if cuccos on screen is 1 or the ones that have disappeared is >= 3, then it resets angry, hits, and disappeared 
        if Cucco.num_cucco <= 1 or Cucco.num_cuccos_disappeared >=3:
            Cucco.cuccos_angry = False
            Cucco.num_hits = 0
            Cucco.num_cuccos_disappeared = 0
        #if cuccos aren't angry, then the just bounce around the screen. If they are then they flock to link
        if Cucco.cuccos_angry == False:
            self.x += self.x_direction * self.speed
            self.y += self.y_direction * self.speed
        # if they are angry, it checks if they are attached to link and then starts decreasing the timer
        # if the timer reaches 0, then they valid is set to false so they can be removed, and num cuccos decreases while disappeared increases
        elif Cucco.cuccos_angry == True:
            if self.attached_to_link == True:
                self.attached_to_link_timer -=1
                if self.attached_to_link_timer == 0:
                    self.valid = False
                    Cucco.num_cucco -=1
                    Cucco.num_cuccos_disappeared +=1
            else:
                #find Link and fly to him
                dx = Cucco.linkx - self.x
                dy = Cucco.linky - self.y
                length = math.sqrt(dx*dx + dy*dy)
                #make sure the distance isn't 0
                if length < 0.001:
                    length = 0.001
                direction_to_go_x = dx / length
                direction_to_go_y = dy / length
                self.x += direction_to_go_x * self.angry_speed
                self.y += direction_to_go_y * self.angry_speed  
        
        #animating the cuccos
        #increasing the image frame 
        self.image_frame += 1

        #using %2 to rotate back through images.
        self.image_frame = self.image_frame %2

        #updates which cucco to show. Normal or angry
        if Cucco.cuccos_angry == False:
            self.image = self.images[self.direction][self.image_frame]
        else:
            self.image = self.angry_images[self.direction][self.image_frame]

        return self.valid
    

    #boolean to show a cucco is a cucco, returns true
    def is_cucco(self):
        return True

    # String method that returns cucco's x,y,w,h
    def to_string(self):
        return "Cucco (x,y) = (" + str(self.x) + ", " + str(self.y) + "), w = " + str(self.w) + ", h = " + str(self.h)
    
    # collision fixing for cucco
    def cucco_fix_collision(self, t):
        if self.px + self.w <= t.x and self.x + self.w >= t.x:
            self.x = t.x - self.w
            self.x_direction = -self.x_direction
        elif self.px >= t.x + t.w and self.x < t.x + t.w:
            self.x = t.x + t.w
            self.x_direction = -self.x_direction
        if self.py + self.h <= t.y and self.y + self.h >= t.y:
            self.y = t.y - self.h
            self.y_direction = -self.y_direction
        elif self.py >= t.y + t.h and self.y <= t.y + t.h:
            self.y = t.y + t.h
            self.y_direction = -self.y_direction

class Model():
    filename = "map.json"
    
    def __init__(self):
        self.load_map()

        #track current item 
        self.item_num = 0

        #list to add sprites
        self.items_i_can_add = []

        #adding the sprites to the list
        self.items_i_can_add.append(Tree(0,0, 60, 60))
        self.items_i_can_add.append(TreasureChest(0,0))
        self.items_i_can_add.append(Cucco(0,0))



    def load_map(self):
        # reset the tree count if we're loading (or reloading)
        # the map
        Tree.reset_tree()
        
        self.sprites = []
        # example of adding a hardcoded tree
        self.sprites.append(Tree(200,100,Tree.TREE_WIDTH, Tree.TREE_HEIGHT))
        
        # example of reading through the map.json file
        # and loading tree and the link's location
        # open the json map and pull out the individual lists of sprite objects
        with open(Model.filename) as file:
            data = json.load(file)
            #get the lists from the map.json file
            trees = data["trees"]
            treasurechests = data["treasurechests"]
            cuccos = data["cuccos"]
            #get link data out - these are individual
            #attributes, not a list
            linkx = data["linkx"]
            linky = data["linky"]
        file.close()
        
        #create link using saved attributes
        self.link = Link(linkx, linky)
        self.sprites.append(self.link)

        #for each entry inside the trees list, pull the key:value pair out and create 
        #a new tree object with (x,y,w,h)
        for entry in trees:
            self.sprites.append(Tree(entry["x"], entry["y"], Tree.TREE_WIDTH, Tree.TREE_HEIGHT))

        #entry inside the treasure chest list, it pulls the key:value pair out and creates a new chest object (x,y,w,h)
        for entry in treasurechests:
            self.sprites.append(TreasureChest(entry["x"], entry["y"]))

        #entry inside the cuccos list, it pulls the key:value pair out and creates a new chest object (x,y,w,h)
        for entry in cuccos:
            self.sprites.append(Cucco(entry["x"], entry["y"]))

    def save_map(self):
        # create lists for each type of sprite you want to save
        trees = []
        treasurechests = []
        cuccos = []

        # go through all of the sprites, saving them into the 
        # appropriate lists
        for s in self.sprites:
            if s.is_tree():
                trees.append(s.marshal())
            elif s.is_treasure_chest():
                treasurechests.append(s.marshal())
            elif s.is_cucco():
                cuccos.append(s.marshal())

        # create the dictionary of sprites, split by what types
        # they are - tree, chests, cuccos are lists, while 
        # linkx and linky are singular attributes
        map_to_save = {
            "trees": trees,
            "treasurechests": treasurechests,
            "cuccos": cuccos,
            "linkx": self.link.x,
            "linky": self.link.y
        }

        # Save to file
        with open(Model.filename, "w") as f:
            json.dump(map_to_save, f)

    def update(self):
        #for sprite in self.sprites:
           # sprite.update()
        #outer loop updates the sprite and checks if its still valid
        # I kept getting a list index out of range error after a couple boomerangs and chests were removed
        # to fix this, i did a negative index to start from the end of the list
        for i in range(len(self.sprites) -1, -1, -1): 
            s1 = self.sprites[i]
            if not s1.update(): # if its not, then its removed
                del self.sprites[i]
                continue
            # inner loop checks for and fixes collisions
            for j in range(len(self.sprites)):
                if i == j:
                    continue 
                s2 = self.sprites[j]
                if self.is_there_a_collision(s1, s2):
                    #collision fixing between link and tree
                    if s1.is_link() and s2.is_tree():
                        self.link.fix_collision(s2)
                    #collision between boomerang and tree
                    if s1.is_boomerang() and s2.is_tree():
                        s1.boomerang_destroyed()
                    #collision between link and unopened chest and it turns into an open chest(rupee)
                    if s1.is_link() and s2.is_treasure_chest():
                        if s2.chest_opened == False:
                            s2.treasure_chest_opened()

                        elif s2.chest_opened == True and s2.frames_since_opened >= 10:
                            s2.valid = False
                            self.link.gems_gathered()

                        elif s2.chest_opened == True and s2.frames_since_opened < 10:
                            pass
                    #collision between a boomerang and chest. This also handles the boomerang and rupee
                    if s1.is_boomerang() and s2.is_treasure_chest():
                        if s2.chest_opened == False:
                            s2.treasure_chest_opened()
                            s1.boomerang_destroyed()
                        elif s2.chest_opened == True and s2.frames_since_opened >= 10:
                            s2.valid = False
                            s1.boomerang_destroyed()
                            self.link.gems_gathered()
                        elif s2.chest_opened == True and s2.frames_since_opened < 10:
                            s1.boomerang_destroyed()
                    #collision handling between cucco and tree
                    if s1.is_cucco() and s2.is_tree():
                        if Cucco.cuccos_angry == True:
                            pass
                        elif Cucco.cuccos_angry == False:
                            s1.cucco_fix_collision(s2)
                    #collision between cucco and chest
                    if s1.is_cucco() and s2.is_treasure_chest():
                        if Cucco.cuccos_angry == True:
                            pass
                        elif Cucco.cuccos_angry == False:
                            s1.cucco_fix_collision(s2)

                    #collision between boomerang and cucco
                    #fixes the collison but also increments the num_hits and if they reach 5 or greater, they get angry
                    if s1.is_cucco() and s2.is_boomerang():
                        s1.cucco_fix_collision(s2)
                        Cucco.num_hits +=1
                        if Cucco.num_hits >= 5:
                            Cucco.cuccos_angry = True     
                 
                    #collision between cucco and link
                    #fixes the collison but also increments the num_hits and if they reach 5 or greater, they get angry
                    if s1.is_cucco() and s2.is_link():
                        if Cucco.cuccos_angry == True:
                            s1.attached_to_link = True
                            s1.attached_to_link_timer = 20
                        else:
                            s1.cucco_fix_collision(s2)
                            Cucco.num_hits +=1
                            if Cucco.num_hits >= 5:
                                Cucco.cuccos_angry = True


    def clear_map(self):
        self.sprites.clear()
        self.sprites.append(self.link)
        # calling a static method - notice the lack of 'self'
        Tree.reset_tree()

    # pos was passed as the mouse position tuple - pos[0] is x, 
    # pos[1] is y
    #adding sprites to the map 
    def add_sprites(self, pos):
        for i in range(len(self.sprites)):
            sprite = self.sprites[i]
            if sprite.detecting_sprites(pos[0], pos[1]):
                print("There is already a sprite here!")
                return
        current_item = self.get_current_item()
        if current_item is None:
            return 
        if current_item.is_tree():
            self.sprites.append(Tree(pos[0], pos[1]))
        if current_item.is_treasure_chest():
            self.sprites.append(TreasureChest(pos[0], pos[1]))
        if current_item.is_cucco():
            self.sprites.append(Cucco(pos[0], pos[1]))

    #method to cycle through the addable items
    def cycle_through_items(self):
        self.item_num = (self.item_num + 1) % len(self.items_i_can_add)
    #method that calls the previous position method from link
    def tell_link_to_save_current_position(self):
        self.link.save_previous_position()

    #method that calls link's moving method
    def move_link(self, direction):
        self.link.move_self(direction)

    #collision detection between two sprites     
    def is_there_a_collision(self, spriteA, spriteB):
        if spriteA.x >= spriteB.x + spriteB.w:
            return False
        if spriteA.x + spriteA.w <= spriteB.x:
            return False
        if spriteA.y >= spriteB.y + spriteB.h:
            return False
        if spriteA.y + spriteA.h <= spriteB.y:
            return False
        return True
    
    #adds the boomerang using link's x,y and direction
    def add_boomerang(self):
        self.sprites.append(Boomerang(self.link.x, self.link.y, "images/boomerang1.png", self.link.direction))

    #method that returns the item 
    def get_current_item(self):
        return self.items_i_can_add[self.item_num]

class View():
    VIEW_WIDTH = 750
    VIEW_HEIGHT = 500

    def __init__(self, model):
        SCREEN_SIZE = (View.VIEW_WIDTH, View.VIEW_HEIGHT)
        self.screen = pygame.display.set_mode(SCREEN_SIZE, 32)
        self.model = model
        #room x and y variables
        self.current_room_x = 0
        self.current_room_y = 0

    def update(self):
        # change background color if the user is in edit_mode
        if Controller.edit_mode:
            self.screen.fill([146, 203, 146]) #light green
            pygame.draw.rect(self.screen, (124,255,0), (0,0,75,75)) #drawing a green box in the left corner when in edit mode
            if self.model.get_current_item() != None: # checking if current item isnt null or none
                self.model.get_current_item().draw_yourself(self.screen, -5, -5) #draws the current item in the green box 
        else:
            self.screen.fill([72, 152, 72]) #dark forest green

        #handles the room transitions for link. The "camera" for the game when link moves left and right between rooms
        room_x_multiplier = math.floor((self.model.link.x + self.model.link.w/2)/View.VIEW_WIDTH)
        self.current_room_x = room_x_multiplier * View.VIEW_WIDTH

        #this is for when link goes up and down between rooms
        room_y_multiplier = math.floor((self.model.link.y + self.model.link.h/2)/View.VIEW_HEIGHT)
        self.current_room_y = room_y_multiplier * View.VIEW_HEIGHT
        
        # draw sprites to the screen. Calls the draw method from sprite
        for sprite in self.model.sprites:
            sprite.draw_yourself(self.screen, self.current_room_x, self.current_room_y)


        # add text to the screen
        # Default font, size 32
        font = pygame.font.SysFont(None, 32)   
        text_string = "Rupees gathered: " + str(self.model.link.num_gems_gathered)
        WHITE_COLOR = (253, 251, 249)
        text_surface = font.render(text_string, True, WHITE_COLOR)
        TEXT_LOCATION = (500, 10)
        self.screen.blit(text_surface, TEXT_LOCATION)
        
        # update display screen
        pygame.display.flip()

class Controller():
    edit_mode = False
    
    def __init__(self, model, view):
        self.model = model
        self.view = view
        self.keep_going = True

    def update(self):
        for event in pygame.event.get():
            if event.type == QUIT:
                self.keep_going = False
            elif event.type == KEYDOWN:
                if event.key == K_ESCAPE or event.key == K_q:
                    self.keep_going = False
            elif event.type == pygame.MOUSEBUTTONUP:
                mouse_pos = pygame.mouse.get_pos()
                mouse_x = mouse_pos[0]
                mouse_y = mouse_pos[1]
                if Controller.edit_mode:
                    if mouse_x >= 0 and mouse_x <= 75 and mouse_y >= 0 and mouse_y <= 75: #checking to see if we're clicking int the bounds of the green box
                        self.model.cycle_through_items() #cycles through items
                    else:
                        # adds sprites 
                        x = (mouse_x // 75) * 75
                        y = (mouse_y // 75) * 75
                        self.model.add_sprites((x + self.view.current_room_x, y + self.view.current_room_y))
            elif event.type == pygame.KEYUP: #this is keyReleased!
                if event.key == K_c:
                    self.model.clear_map()
                    print("Map cleared and game reset")
                if event.key == K_e:
                    Controller.edit_mode = not Controller.edit_mode
                if event.key == K_l:
                    self.model.load_map()
                    print("Map loaded")
                if event.key == K_s:
                    self.model.save_map()
                    print("Map saved")
        keys = pygame.key.get_pressed()
        # movement function changed to be closer related
        # to Link
        self.model.tell_link_to_save_current_position()
        if keys[K_LEFT]:
            self.model.link.move_self("left")
        if keys[K_RIGHT]:
            self.model.link.move_self("right")
        if keys[K_UP]:
            self.model.link.move_self("up")
        if keys[K_DOWN]:
            self.model.link.move_self("down")
        if keys[K_SPACE]:
            self.model.add_boomerang()

        #setting link's location as a static class member in Cucco so they know where he's at
        Cucco.linkx = self.model.link.x
        Cucco.linky = self.model.link.y

print("Use the arrow keys to move. Press Esc to quit.")
pygame.init()
pygame.font.init()
m = Model()
v = View(m)
c = Controller(m, v)
while c.keep_going:
    c.update()
    m.update()
    v.update()
    sleep(0.04)
print("Goodbye!")