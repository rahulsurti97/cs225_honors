(ns adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:gen-class))

(def the-map
  { :ISR
       {:enable true
        :seen false
        :has-found true
        :default-desc "The food doesn't look very appetizing..."
        :first-desc "You realize you need your backpack!\n--Press 'i' to pick up items or interact with people.\n--Press 'l' to look around the room."
        :second-desc "Maybe you should head to Grainger Library since you were studying with him yesterday.\n--Use 'w', 'a', 's', 'd' to move North, West, South, and East"
        :title "_ISR_"
        :dir {:south :CRCE
              :east :CircleK
              :west :Loomis}
        :contents :coffee-mug
        :find "Nothing to look for here."
        :first-message "--Press 'b' to see what you have in your bag."
        :second-message "There's nothing you need from your room right now."
        :set-enable {:Grainger true}}

    :CircleK
       {:enable false
        :seen false
        :has-found false
        :default-desc "It smells like gas over here."
        :first-desc ""
        :second-desc ""
        :title "_Circle K_"
        :dir {:west :ISR}
        :contents nil}
    :CRCE
       {:enable false
        :seen false
        :has-found false
        :default-desc "The squat rack is taken."
        :first-desc "You know you're close to finding out the truth..."
        :second-desc "Man, it really smells in here."
        :title "_CRCE_"
        :dir {:north :ISR
              :west  :Foellinger}
        :contents :shirt
        :find "You search through every area, but you don’t see him anywhere! When you check the locker room, you find a button-down shirt on the floor and see that his initials are written on the tag. He probably dropped it when he changed into gym clothes."
        :first-message "You take the shirt and leave, feeling discouraged that he isn’t there.  Gosh, if only I could get a better hold of him... Ah!  I could send him a text!\n--Press 'x' to send a text."
        :second-message "Lifting on an empty stomach is probably not the best idea."
        :set-enable {:Brothers true}}
    :Loomis
       {:enable false
        :seen false
        :has-found false
        :default-desc "There are some experiments going on."
        :first-desc "When you look around at the classrooms, you see that one room has all the windows covered. This is strange..."
        :second-desc ""
        :title "_Loomis Laboratory_"
        :dir {:north :Seibel
              :south :Noyes
              :east  :ISR
              :west  :Union}
        :contents :your-phone
        :find "You knock on the door. It opens just a crack, and you see your roommate poke his head out! He tries to slam the door in your face, but you think more quickly and toss the drumstick to the floor. It lodges in the foot of the door, keeping your roommate from closing and locking it. You force the door open and find a huge science experiment being put together on the floor. There’s your laptop connected by wires to a large machine! The red guitar is hooked up to a different contraption, and you look at your roommate in horror. Your cellphone is sitting on a nearby desk unused!"
        :first-message "You run to it before your roommate can stop you. You quickly dial 911 and yell your address into the phone as your roommate tries to rip it from your hands. You fend him off and push him to the ground as sirens blare in the distance. Within a few minutes, police officers barge in and grab your roommate. Turns out, he was trying to build a weapon of mass destruction to destroy the entire school! Thank goodness you were there to save the day. Good work!"}
    :Seibel
       {:enable false
        :seen false
        :has-found false
        :default-desc "There are a lot of nerds here."
        :first-desc "Good thing your roommate's girlfriend is working her shift in Einstein's today!  You should go over to her."
        :second-desc "Maybe your roommate's girlfriend will give you a free bagel..."
        :title "_Seibel Center_"
        :dir {:south :Loomis
              :west  :ECEB}
        :contents :phone
        :find "You walk over to Einstein's to explain the situation and ask if she’s heard from him. She mentions that he texted her two hours ago that he was going to get lunch at Maize, and she generously offers to give you her cell phone to try contacting him, asking only that you return it by nighttime."
        :first-message "You thank her and take the phone.\n--Press 'p' to use the phone."
        :second-message "Nevermind, you don't have time for that."
        :set-enable {:Maize true}}
    :ECEB
       {:enable false
        :seen false
        :has-found false
        :default-desc "So many computers."
        :first-desc "You see some guys messing around with lab equipment."
        :second-desc "They guys really seem to be struggling with their breadboard."
        :title "_ECEB_"
        :dir {:south :Grainger
              :east  :Seibel}
        :contents :wires
        :find "You go over and ask if they’ve seen your roommate. One nods and the other says he took some expensive equipment with him to Loomis, which everyone knows is against the rules.  They offer you some wires since they have so many."
        :first-message "You take them because why not?"
        :second-message "You probably shouldn't take any more, looks like they're going to need them."
        :set-enable {:Loomis true}}

    :Grainger
       {:enable false
        :seen false
        :has-found false
        :default-desc "This place is empty."
        :first-desc "You're glad that you're inside again, it's freezing outside.  Time to start looking for clues.\n--Press 'f' to find people or things of interest."
        :second-desc "You can pick up a coffee from Espresso.  Good thing your friend works there!"
        :title "_Grainger Library_"
        :dir {:north :ECEB
              :south :Union
              :east  :Seibel}
        :contents :reading-glasses
        :find "Oh look!  You see your friends sitting at a table.  You go over and talk to them and realize that you had forgotten your reading glasses here last night."
        :first-message "You check the lost and found and luckily find your reading glasses.  These will come in handy!\nNow that you've gotten that out of the way, maybe his girlfriend who works at Einstein's in Seibel knows something!"
        :second-message "--Press 'g' to grab some coffee from locations with coffee shops.\n--Press 'c' to drink some coffee.\n   The caffeine speeds up your brain and slows down time.\n   It's almost as if you went back in time by half an hour!\n   Too bad your caffeine tolerance is pretty low and you can only drink up to 3 cups a day.\n--Press 't' to check the time on your watch."
        :set-enable {:ISR false
                     :Seibel true}}
    :Union
       {:enable false
        :seen false
        :has-found false
        :default-desc "You smell coffee."
        :first-desc "You head to the basement.  Time to start looking for clues."
        :second-desc "All these nasty food places remind you that you roommate is in Chem 102.  Maybe the lab report he was working on last night can give you some insight."
        :title "_Illini Union_"
        :dir {:north :Grainger
              :south :Quad
              :east  :Loomis
              :west  :Brothers}
        :contents :car-keys
        :find "No roommate in sight, but you do see a sweaty guy sipping a soda nearby and chat with him. Turns out you were right and your roommate was here dancing his butt off.  Not only that the the sweaty guy happened to be going to the lost and found later because he found your roommate's car keys!  They must've fallen out of his pocket when he was competing!"
        :first-message "You thank the sweaty guy for handing you the car keys and wonder what you should do next..."
        :second-message "What you would do to grab some grub right now..."
        :set-enable {:Noyes true}}
    :Quad
       {:enable false
        :seen false
        :has-found false
        :default-desc "It's really cold."
        :first-desc "It's really cold."
        :second-desc "It's really cold."
        :title "_Main Quad_"
        :dir {:north :Union
              :south :Foellinger
              :east :Noyes
              :west :Altgeld}
        :contents nil
        :set-enable {:CRCE true}}
    :Brothers
       {:enable false
        :seen false
        :has-found false
        :default-desc "There's a lot of drunk people."
        :first-desc "You realize you don’t have your license, but you take your chances and hope that you look old enough to not be carded."
        :second-desc ""
        :title "_Brother's Bar_"
        :dir {:east :Union
              :west :Maize}
        :contents :flyer
        :first-message "You picked up the flyer."
        :second-message "If only you had the leisure of drinking right now."
        :find "They don’t buy it, so you turn around and walk back down to the door. You notice a flyer on the ground."
        :set-enable {:Altgeld true}}
    :Maize
       {:enable false
        :seen false
        :has-found false
        :default-desc "It smells really good in here."
        :first-desc "It's really crowded here, but you should probably squeeze through to the cashier."
        :second-desc "You hear you stomach grumble..."
        :title "_Maize_"
        :dir {:east  :Brothers}
        :contents :reciept
        :first-message "You graciously accept it.\n--Press 'r' to read."
        :second-message "Man, you sure wish you had your credit card right about now."
        :find "You describe your roommate and ask them if they remember seeing him earlier. They angrily say that yes, they did see him, and that he was extremely rude asking them to rush his order.  They offer you a reciept."
        :set-enable {:Union true}}
    :Altgeld
       {:enable false
        :seen false
        :has-found false
        :default-desc "All this math hurts your brain."
        :first-desc "Hey!  You remember that he had a package notification to pick up something from the post office there."
        :second-desc ""
        :title "_Altgeld_"
        :dir {:north :Grainger
              :south :Foellinger
              :east :Quad}
        :contents :note
        :first-message "They hand you the note."
        :second-message ""
        :find "When you arrive, they tell you it has already been picked up but that a piece of paper had fallen out."
        :set-enable {:Foellinger true}}
    :Noyes
       {:enable false
        :seen false
        :has-found false
        :default-desc "There are lots of chemicals here."
        :first-desc "Maybe his TA is in the building..."
        :second-desc "The awful cell reception in here reminds you that you still have your roommate's girlfriend's cell phone!  You should prob go somewhere open to call him."
        :title "_Noyes_"
        :dir {:north :Loomis
              :south :Foellinger
              :east  :ISR
              :west  :Quad}
        :contents :lab-report
        :find "You go to his TA’s office to ask if he’s turned in his Chem 102 lab. The TA shrugs and offers you a stack of labs to sort through to see if his is there."
        :first-message "You find his lab report third from the top, and when the TA looks away you slip it in your backpack, knowing your roommate will fail the class if he gets a 0% on this."
        :second-message "You can't take any of these chemicals out of the building."
        :set-enable {:Quad true
                     :CRCE true}}
    :Foellinger
       {:enable false
        :seen false
        :has-found false
        :default-desc "There's a performace going on."
        :first-desc "You see a bunch of guys on stage searching frantically for something."
        :second-desc "You frantically look around to think where to go."
        :title "_Foellinger_"
        :dir {:north :Quad
              :south :UGL
              :east  :CRCE}
        :contents :drumstick
        :find "You rush to the front of the stage and explain what likely happened.  The guitarist says he needs that guitar back before their performance tonight! You tell them you’ll find it, and the drummer asks if you want a little something for the road."
        :first-message "He tosses you his drumstick to keep and says thanks for your help."
        :second-message "That dude is so cool."
        :set-enable {:UGL true
                     :ECEB true}}

    :UGL
       {:enable false
        :seen false
        :has-found false
        :default-desc "There's a lot of books here."
        :first-desc "Your roommate's friends always hang out at UGL, maybe they're here."
        :second-desc "You don't see anyone else here."
        :title "_UGL_"
        :dir {:north :Foellinger}
        :contents nil
        :find "You see his girlfriend again, and you give her phone back because you know it’s no use now. She suggests checking out the ECEB."
        :first-message ""
        :second-message ""
        :set-enable {}}})

(def adventurer
  {:location :ISR
   :inventory #{}
   :tick 0
   :seen #{}
   :coffee-mug-status false
   :drank-coffee 0})

(defn print-time [player]
  (def t (player :tick))
  (def h (quot (* t 5) 60))
  (if (and (>= t 0) (< t 12)) (def h 12))
  (if (and (< t 0) (> t -12)) (def h 11))
  (if (<= t -12) (def h 10))
  (def m (mod (* t 5) 60))
  (if (= h 6)
    (do (println "Oh no, it's 6 o'clock!  You're roomate is definitely gone by now...")
      (System/exit 0)))
  (print (str h ":"))
  (if (< m 10)
    (println (str 0 m " PM"))
    (println (str m " PM")))
  player)

(defn status [player]
  (let [location (player :location)]
    (println (str "\n" (-> the-map location :title)))

    (if (not (-> the-map location :seen))
      (if (-> the-map location :enable)
        (if (contains? (get-in player [:inventory]) (-> the-map location :contents))
          (println (-> the-map location :second-desc))
          (println (-> the-map location :first-desc)))
        (println (-> the-map location :default-desc))))
    (def the-map (assoc-in the-map [location :seen] true))
    player))
    ; (when-not ((player :seen) location)
    ;   (println (-> the-map location :desc)))
    ; (update-in player [:seen] #(conj % location))))

(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))

(defn tock [player]
  (update-in player [:tick] inc))


(defn go [dir player]
   (let [location (player :location)
         dest (->> the-map location :dir dir)]
    (if (nil? dest)
      (do (println "You can't go that way.")
        player)
      (tock (assoc-in player [:location] dest)))))

(defn item [player]
  (let [location (player :location)
        item (->> the-map location :contents)
        found (->> the-map location :has-found)]

    (if (or (= nil item) (not found))
      (do (println "There isn't anything here.")
        player)
      (if (contains? (get-in player [:inventory]) item)
        (do (println (-> the-map location :second-message)) ; after user picks up item from location
          player)
        (do (println (-> the-map location :first-message))  ; before user picks up item from location
          ;enable and disable the next and previous parts of the story
          (if (= location :Loomis)
            (System/exit 0))
          (let [to-enable (-> the-map location :set-enable)]
            (doseq [loc (keys to-enable)]
              (let [boolean (get-in to-enable [loc])])
              (if boolean
                (def the-map (assoc-in the-map [loc :seen] false)))
              (def the-map (assoc-in the-map [loc :enable] boolean))))
          (update-in player [:inventory] #(conj % item)))))))

(defn bag [player]
  (let [bag (get-in player [:inventory])]
    (if (empty? bag)
      (do (println "Your bag is empty.")
        player)
      (do (println "You have:")
        (doseq [item bag]
          (println (str " " (str/replace (name item) #"-" " "))))
        player))))

(defn find-room [player]
  (let [location (player :location)
        clue (->> the-map location :find)]
    (if (or (= nil clue) (not (-> the-map location :enable)))
      (do (println "There aren't any clues here.")
        player)
      (do
        (if (or (= location :UGL)
                (= location :Quad)
                (= location :ECEB))
          (let [to-enable (-> the-map location :set-enable)]
            (doseq [loc (keys to-enable)]
                (let [boolean (get-in to-enable [loc])]
                  (if boolean
                    (def the-map (assoc-in the-map [loc :seen] false)))
                  (def the-map (assoc-in the-map [loc :enable] boolean))))))
        (println clue)
        (def the-map (assoc-in the-map [location :has-found] true))
        player))))

(defn look [player]
  (let [location (player :location)]
    (def the-map (assoc-in the-map [location :seen] false))
    player))

(defn grab-coffee [player]
  (let [location (player :location)]
    (if (or (= location :Grainger)
            (= location :Union)
            (= location :ECEB)
            (= location :Seibel)
            (= location :UGL)
            (= location :CircleK))
      (if (contains? (get-in player [:inventory]) :coffee-mug)
        (if (get-in player [:coffee-mug-status])
          (do (println "Your coffee mug is already full.")
            player)
          (do (println "You filled your coffee mug!")
            (assoc-in player [:coffee-mug-status] true)))
        (do (println "You need your coffee mug to get free coffee.")
          player))
      (do (println "This place doesn't serve coffee.")
        player))))

(defn drink-coffee [player]
  (if (contains? (get-in player [:inventory]) :coffee-mug)
    (if (get-in player [:coffee-mug-status])
      (if (< (get-in player [:drank-coffee]) 3)
        (do (println "You downed your coffee mug!")
          (assoc-in (update-in (assoc-in player [:tick] (- (get-in player [:tick]) 6)) [:drank-coffee] inc) [:coffee-mug-status] false))
        (do (println "You've already drank 3 cups of coffee... Maybe you should take it easy for the rest of the day.")
          player))
      (do (println "Your mug is empty.")
        player))
    (do (println "You need your coffee mug to drink coffee.")
      player)))

(defn read-paper [player]
  (if (contains? (get-in player [:inventory]) :note)
    (println "The note says \"Enjoy these cookies, see you with the goods soon. Be sure to grab the red guitar, not the black one\". You’re confused but suddenly understand that he is planning on stealing from multiple people today!  Good thing you grabbed the flyer off the ground!")
    (if (contains? (get-in player [:inventory]) :flyer)
      (println "Looks like there's a performance going on at Foellinger today.")
      (if (contains? (get-in player [:inventory]) :reciept)
        (println "You pull our your reading glasses and see that your stealing jerk of a roommate used your credit card!  Hmm... after such a nice meal, your roommate could be looking for a way to work off the meal.  He must be in the basement of the Union playing Dance Dance Revolution!")
        (println "You don't have anything to read."))))
  player)

(defn phone [player]
  (if (contains? (get-in player [:inventory]) :phone)
    (if (and (-> the-map :Quad :enable) (= (get player :location) :Quad))
      (do (println "\"Hello?\"\n\n You muffle your voice with a scarf and ask him where he is. He says he’s at CRCE working out.")
        player)
      (do (println "There's no answer.")
        player))
    (do (println "You don't have your phone with you.")
      player)))

(defn send-text [player]
  (if (contains? (get-in player [:inventory]) :phone)
    (if (-> the-map :CRCE :enable)
      (do (println "You text him and ask if he wants to meet you at Brothers for happy hour (you know he still thinks you’re the girl – so sneaky). He replies yes.  You’re lucky to have found his shirt at CRCE.  You put it on to look a little nicer before going in to the bar.  ")
        player)
      (do (println "You don't know what to say, maybe you should hold off texting him for now.")
        player))
    (do (println "You don't have your phone with you.")
      player)))

(defn quit [player]
  (if (contains? (get-in player [:inventory]) :car-keys)
    (println "Well, your roommate can't leave campus without his car keys, but finding him will be impossible...")
    (println "Well, your roommate is definitely gone by now.  At least it's almost Christmas..."))
  (System/exit 0))

(defn respond [player command]
      (match command
          [:l] (look player)
          [:w] (go :north player)
          [:s] (go :south player)
          [:d] (go :east player)
          [:a] (go :west player)
          [:i] (item player)
          [:b] (bag player)
          [:t] (print-time player)
          [:p] (phone player)
          [:r] (read-paper player)
          [:f] (find-room player)
          [:c] (drink-coffee player)
          [:g] (grab-coffee player)
          [:q] (quit player)
          [:x] (send-text player)
            _  (do (println "I don't understand you.")
                  player)))

(defn -main
  [& args]
  (println "The Case of the Thieving Roommate\n\n'Twas noon after the last day of school and not a creature was stirring, not even...your roommate.  That's weird, he's always up and about at this time.  You crack open your eyes and see that not only is your roommate gone, but his entire half of the room has been cleared out!\n\nYou’re confused at first because he didn’t mention that he’d be moving out next semester, but then you realize that your brand new laptop, cellphone, and wallet are all missing from your desk.\n\nFurious, you jump up and get dressed, knowing you have to track this guy down before he drives home for winter break at 6 PM tonight! You don’t even know where to start, but you know you have to get moving. Hurry before time runs out!\n\n--Press 'q' to quit.")
  (loop [local-map the-map
         local-player adventurer]
    (let [pl (status local-player)
          _  (println "What do you want to do?")
          command (read-line)]
      (recur local-map (respond pl (to-keywords command))))))
