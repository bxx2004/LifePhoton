rootProject.name = "LifePhoton"
include("module:authentication")
include("common")
include("module:file-management")
include("module:genome")
include("module:mating-type-imputation")
include("tester")
include("module:view-controller")
findProject(":module:view-controller")?.name = "view-controller"
