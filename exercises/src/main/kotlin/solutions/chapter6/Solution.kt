package solutions.chapter6

//
// "DDD Framework"
//

interface EntityEvent

interface EntityState<in E : EntityEvent> {
    fun combine(event: E): EntityState<E>
}

//
// Events
//

sealed class ElevatorEvent: EntityEvent

data class ElevatorCalled(val atFloor: Int): ElevatorEvent()

sealed class ElevatorDoorOpen: ElevatorEvent()

sealed class ElevatorDoorClosed: ElevatorEvent()

data class ElevatorAscending(val fromFloor: Int, val toFloor: Int): ElevatorEvent()

data class ElevatorDescending(val fromFloor: Int, val toFloor: Int): ElevatorEvent()

sealed class ElevatorStopped: ElevatorEvent()

//
// States
//

sealed class ElevatorState : EntityState<ElevatorEvent> {
    abstract override fun combine(event: ElevatorEvent): ElevatorState
}

data class ElevatorIdle internal constructor(
    val floor: Int,
    val isDoorOpen: Boolean,
    val requestedFloors: Set<Int>,
    val travelPlan: List<Int>):  ElevatorState() {
    override fun combine(event: ElevatorEvent): ElevatorState =
        when (event) {
            is ElevatorCalled -> this.request(event.atFloor)
            is ElevatorDoorOpen -> this.handleDoor(true)
            is ElevatorDoorClosed -> this.handleDoor(false)
            is ElevatorAscending -> this.ascend()
            is ElevatorDescending -> this.descend()
            is ElevatorStopped -> this
        }
}

data class ElevatorTravelling internal constructor(
    val fromFloor: Int,
    val toFloor: Int,
    val requestedFloors: Set<Int>,
    val travelPlan: List<Int>):  ElevatorState() {
    override fun combine(event: ElevatorEvent): ElevatorState =
        when (event) {
            is ElevatorCalled -> this.request(event.atFloor)
            is ElevatorStopped -> this.stop()
            else -> this // Ignore other events
        }
}

//
// State Transitions
//

fun ElevatorIdle.request(atFloor: Int): ElevatorIdle =
    ElevatorIdle(
        this.floor,
        this.isDoorOpen,
        this.requestedFloors.plus(atFloor),
        this.travelPlan.toList())


fun ElevatorIdle.handleDoor(isDoorOpen: Boolean) =
    ElevatorIdle(
        this.floor, isDoorOpen,
        this.requestedFloors.toSet(),
        this.travelPlan.toList())

fun ElevatorIdle.ascend(): ElevatorTravelling =
    if (this.travelPlan.isEmpty()) {
        val travelPlan = this.requestedFloors.sorted()
        ElevatorTravelling(
            this.floor,
            travelPlan.first(),
            emptySet(),
            travelPlan)
    } else {
        ElevatorTravelling(
            this.floor,
            travelPlan.first(),
            this.requestedFloors.toSet(),
            this.travelPlan.toList())
    }


fun ElevatorIdle.descend(): ElevatorTravelling =
    if (this.travelPlan.isEmpty()) {
        val travelPlan = this.requestedFloors.sortedDescending()
        ElevatorTravelling(
            this.floor,
            travelPlan.first(), emptySet(),
            travelPlan)
    } else {
        ElevatorTravelling(
            this.floor,
            travelPlan.first(),
            this.requestedFloors.toSet(),
            this.travelPlan.toList())
    }

fun ElevatorTravelling.request(atFloor: Int): ElevatorTravelling = ElevatorTravelling(
    this.fromFloor,
    this.toFloor,
    this.requestedFloors.plus(atFloor),
    this.travelPlan.toList())


fun ElevatorTravelling.stop(): ElevatorIdle = ElevatorIdle(
    this.toFloor,
    false,
    this.requestedFloors.toSet(),
    this.travelPlan.toList())

//
// Commands
//

sealed class ElevatorCommand

data class CallElevator(val fromFloor: Int) : ElevatorCommand()

sealed class TravelElevator : ElevatorCommand()

// TODO: Actual command handlers
fun handleCommand(state: ElevatorState, command: ElevatorCommand): ElevatorState {
    return when (command) {
        is CallElevator -> {
            when (state) {
                is ElevatorIdle -> state.request(command.fromFloor)
                is ElevatorTravelling -> state.request(command.fromFloor)
            }
        }
        is TravelElevator -> {
            when (state) {
                is ElevatorIdle ->
                    if (state.requestedFloors.first() > state.floor) state.ascend()
                    else state.descend()
                is ElevatorTravelling -> state
            }
        }
        // ...
    }
}