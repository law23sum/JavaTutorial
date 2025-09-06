rpg/
├── core/
│   ├── Game.java                 # Main game controller
│   ├── GameState.java            # Tracks game progress and relationships
│   ├── GameConfig.java           # Configuration settings
│   └── DramaEngine.java          # Manages story drama and events
├── entities/
│   ├── characters/
│   │   ├── Creature.java         # Abstract base class
│   │   ├── Player.java           # Player character with relationships
│   │   ├── NPC.java              # Non-player characters
│   │   ├── Companion.java        # Characters that can join player
│   │   ├── Rival.java            # Antagonistic characters
│   │   ├── Romanceable.java      # Characters that can be courted
│   │   ├── Monster.java
│   │   ├── Boss.java
│   │   └── StatBlock.java
│   ├── relationships/
│   │   ├── Relationship.java     # Tracks relationship status
│   │   ├── RelationshipType.java # Enum: FRIEND, ROMANCE, RIVAL, etc.
│   │   ├── Friendship.java       # Manages friendship mechanics
│   │   ├── Romance.java          # Manages courtship mechanics
│   │   └── Rivalry.java          # Manages rivalry mechanics
│   ├── items/
│   │   ├── Item.java
│   │   ├── Consumable.java
│   │   ├── Equipment.java
│   │   ├── GiftItem.java         # Items that affect relationships
│   │   ├── Inventory.java
│   │   └── ItemFactory.java
│   └── quests/
│       ├── Quest.java
│       ├── MainQuest.java        # Primary story quest
│       ├── SideQuest.java        # Optional quests
│       ├── RelationshipQuest.java # Quests that affect relationships
│       ├── QuestStep.java
│       └── Reward.java
├── combat/
│   ├── BattleEngine.java         # Turn-based battle system
│   ├── BattleLog.java
│   ├── actions/
│   │   ├── Action.java           # Interface for all actions
│   │   ├── CombatAction.java     # Battle actions
│   │   ├── DialogueAction.java   # Conversation actions
│   │   ├── RelationshipAction.java # Interaction actions
│   │   ├── Command.java          # Command pattern
│   │   └── TurnOrder.java
│   ├── skills/
│   │   ├── Skill.java
│   │   ├── PhysicalSkill.java
│   │   ├── MagicSkill.java
│   │   ├── SocialSkill.java      # Skills that affect relationships
│   │   ├── DamageStrategy.java
│   │   └── CritStrategy.java
│   ├── effects/
│   │   ├── Effect.java
│   │   ├── StatusEffect.java
│   │   ├── RelationshipEffect.java # Effects that modify relationships
│   │   ├── Poison.java
│   │   ├── Burn.java
│   │   ├── Stun.java
│   │   ├── Shield.java
│   │   └── Regen.java
│   └── ai/
│       ├── DecisionMaker.java    # Interface for AI decisions
│       ├── CombatAI.java         # Battle AI
│       ├── SocialAI.java         # Relationship interaction AI
│       ├── BossAI.java
│       └── DramaAI.java          # AI that creates dramatic moments
├── narration/
│   ├── Narrator.java             # Main narrator interface
│   ├── AINarrator.java           # Primary narrator implementation
│   ├── DramaNarrator.java        # Narrates dramatic moments
│   ├── RelationshipNarrator.java # Narrates relationship changes
│   └── NarrationEvent.java
├── dialogue/
│   ├── DialogueSystem.java       # Manages conversations
│   ├── DialogueTree.java         # Branching dialogue structure
│   ├── DialogueNode.java         # Individual dialogue pieces
│   ├── ResponseOption.java       # Player response choices
│   └── DialogueEvent.java        # Events triggered by dialogue
├── drama/
│   ├── DramaticEvent.java        # Significant story moments
│   ├── PlotTwist.java            # Major story twists
│   ├── CharacterArc.java         # Tracks character development
│   └── MoralChoice.java          # Decisions with ethical consequences
├── events/
│   ├── EventBus.java
│   ├── Event.java
│   ├── EventType.java
│   └── EventListener.java
└── utils/
├── IdGenerator.java
├── Dice.java
├── Balance.java
├── Logger.java
└── RelationshipCalculator.java # Calculates relationship changes