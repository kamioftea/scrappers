case class Trait(
    name: String
    description: String
    resources: Map[String, Int]
    tags: List[String]
)

class MemberTrait extends Trait

class GearTrait extends Trait 
